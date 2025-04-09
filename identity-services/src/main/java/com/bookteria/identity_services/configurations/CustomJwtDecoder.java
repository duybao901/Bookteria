package com.bookteria.identity_services.configurations;

import com.bookteria.identity_services.dto.request.IntrospectRequest;
import com.bookteria.identity_services.dto.response.IntrospectResponse;
import com.bookteria.identity_services.exceptions.IdentityException;
import com.bookteria.identity_services.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @Autowired
    private AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            IntrospectResponse response = authenticationService.introspect(
                    IntrospectRequest.builder().token(token).build());

            if (!response.isTokenValid()) throw new IdentityException.TokenEception("Token invalid");
        } catch (JwtException ex) {
//            System.out.println(ex.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HS256");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build();

            OAuth2TokenValidator<Jwt> withClockSkew = new DelegatingOAuth2TokenValidator<>(
                    new JwtTimestampValidator(Duration.ZERO)
            );

            nimbusJwtDecoder.setJwtValidator(withClockSkew);
        }

        return nimbusJwtDecoder.decode(token);
    }
}
