package com.bookteria.identity_services.services;

import com.bookteria.identity_services.dto.request.AuthenticationRequest;
import com.bookteria.identity_services.dto.request.IntrospectRequest;
import com.bookteria.identity_services.dto.request.LogoutRequest;
import com.bookteria.identity_services.dto.request.RefreshTokenRequest;
import com.bookteria.identity_services.dto.response.AuthenticationResponse;
import com.bookteria.identity_services.dto.response.IntrospectResponse;
import com.bookteria.identity_services.entities.InvalidatedToken;
import com.bookteria.identity_services.entities.User;
import com.bookteria.identity_services.exceptions.IdentityException;
import com.bookteria.identity_services.exceptions.UserException;
import com.bookteria.identity_services.repositories.IInvalidatedTokenRepository;
import com.bookteria.identity_services.repositories.IUserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
public class AuthenticationService {
    private final IUserRepository _userRepository;
    private final IInvalidatedTokenRepository _invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.secretKey}") // Inject value from application.yml
    private String SECRET_KEY;

    @NonFinal
    @Value("${jwt.expiration-time}")
    private Long EXPIRATION_TIME;

    @NonFinal
    @Value("${jwt.refreshable-time}")
    private Long REFRESH_TIME;

    public AuthenticationService(IUserRepository _userRepository, IInvalidatedTokenRepository _invalidatedTokenRepository) {
        this._userRepository = _userRepository;
        this._invalidatedTokenRepository = _invalidatedTokenRepository;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        User user = _userRepository.findByUsername(authenticationRequest.getUsername()).orElse(null);
        if (user == null) {
            throw new UserException.UserWithUsernameNotFoundException(authenticationRequest.getUsername());
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean isMatches = false;
        isMatches = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if (!isMatches) {
            throw new IdentityException.PassWordNotMatchException();
        }

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(isMatches)
                .ExpiredTime(Date.from(Instant.now().plus(EXPIRATION_TIME, ChronoUnit.SECONDS)))
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest introspectRequest) {
        var token = introspectRequest.getToken();
        boolean invalid = true;
        try {
            verifyToken(token, false);
        } catch (IdentityException.TokenEception ex) {
            invalid = false;
        }
        return IntrospectResponse.builder()
                .isTokenValid(invalid)
                .build();
    }

    public void logout(LogoutRequest logoutRequest) {
        var signedToken = verifyToken(logoutRequest.getToken(), true);
        invalidateToken(signedToken);
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        SignedJWT signedJWT = verifyToken(refreshTokenRequest.getToken(), true);
        invalidateToken(signedJWT);

        try {
            String username = signedJWT.getJWTClaimsSet().getSubject();
            User user = _userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                throw new UserException.UserWithUsernameNotFoundException(username);
            }

            var token = generateToken(user);

            return AuthenticationResponse.builder()
                    .token(token)
                    .isAuthenticated(true)
                    .ExpiredTime(Date.from(Instant.now().plus(EXPIRATION_TIME, ChronoUnit.SECONDS)))
                    .build();

        } catch (ParseException e) {
            throw new IdentityException.TokenEception("Invalid token format");
        }
    }

    public SignedJWT verifyToken(String token, boolean isRefreshToken) {
        if (token == null || token.trim().isEmpty()) {
            throw new RuntimeException("Token is null or empty");
        }
        try {
            if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
                throw new RuntimeException("SECRET_KEY is not initialized");
            }
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

            SignedJWT signedJWT = SignedJWT.parse(token);

            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            Date expirationTime = (isRefreshToken)
                    ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESH_TIME, ChronoUnit.SECONDS).toEpochMilli())
                    : signedJWT.getJWTClaimsSet().getExpirationTime();

            var isVerified = signedJWT.verify(verifier);
            if (!isVerified)
                throw new IdentityException.TokenEception("Invalid token");

            if (!expirationTime.after(new Date())) {
                throw new IdentityException.TokenEception("Token is expired");
            }

            if (_invalidatedTokenRepository.existsById(jwtId))
                throw new IdentityException.TokenEception("Invalid token");

            return signedJWT;

        } catch (JOSEException e) {
            throw new IdentityException.TokenEception(e.getMessage());
        } catch (ParseException e) {
            throw new IdentityException.TokenEception("Invalid token format");
        }
    }

    public void invalidateToken(SignedJWT signedToken) {
        try {
            String jit = signedToken.getJWTClaimsSet().getJWTID();
            Date expiredTime = signedToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiredTime)
                    .build();

            _invalidatedTokenRepository.save(invalidatedToken);
        } catch (ParseException e) {
            throw new IdentityException.TokenEception("Failed to parse or verify JWT");
        }
    }

    public String generateToken(User user) {
        try {
            // Create JWT Header
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

            // Create Claims
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .jwtID(UUID.randomUUID().toString())
                    .claim("scope", buildScope(user))
                    .issuer("localhost")
                    .issueTime(new Date())
                    .audience("localhost")
                    .expirationTime(Date.from(Instant.now().plus(EXPIRATION_TIME, ChronoUnit.SECONDS)))
                    .build();

            // Create Signed JWT
            SignedJWT signedJWT = new SignedJWT(header, jwtClaimsSet);

            // Sign with HMAC
            if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
                throw new RuntimeException("SECRET_KEY is not initialized");
            }

            JWSSigner signer = new MACSigner(SECRET_KEY.getBytes());
            signedJWT.sign(signer);

            // Serialize to compact form
            return signedJWT.serialize();

        } catch (JOSEException e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add(role.getName());
            });
        }

        return stringJoiner.toString();
    }
}
