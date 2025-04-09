package com.bookteria.identity_services.controllers;

import com.bookteria.identity_services.dto.request.AuthenticationRequest;
import com.bookteria.identity_services.dto.request.IntrospectRequest;
import com.bookteria.identity_services.dto.request.LogoutRequest;
import com.bookteria.identity_services.dto.request.RefreshTokenRequest;
import com.bookteria.identity_services.dto.response.AuthenticationResponse;
import com.bookteria.identity_services.dto.response.IntrospectResponse;
import com.bookteria.identity_services.services.AuthenticationService;
import com.bookteria.shared.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService _authenticationService;

    public AuthenticationController(AuthenticationService _authenticationService) {
        this._authenticationService = _authenticationService;
    }

    @PostMapping("/login")
    public com.bookteria.shared.Result<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        var result = _authenticationService.authenticate(authenticationRequest);
        return Result.success(result);
    }

    @PostMapping("/introspect")
    public Result<IntrospectResponse> introspect(@RequestBody IntrospectRequest introspectRequest) {
        var result = _authenticationService.introspect(introspectRequest);
        return Result.success(result);
    }

    @PostMapping("/logout")
    public Result<?> logout(@RequestBody LogoutRequest logoutRequest) {
        _authenticationService.logout(logoutRequest);
        return Result.success();
    }

    @PostMapping("/refresh")
    public Result<AuthenticationResponse> logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthenticationResponse result = _authenticationService.refreshToken(refreshTokenRequest);
        return Result.success(result);
    }
}
