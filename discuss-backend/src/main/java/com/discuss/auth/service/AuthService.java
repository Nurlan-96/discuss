package com.discuss.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.discuss.auth.dto.LoginRequest;
import com.discuss.auth.dto.LoginResponse;
import com.discuss.auth.security.JwtService;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        // 1. Authenticate the user
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getIdentifier(),
                                request.getPassword()
                        )
                );

        // 2. Generate JWT
        String accessToken = jwtService.generateToken(authentication);

        // 3. Build response
        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setTokenType("Bearer");
        response.setExpiresIn(jwtService.getExpirationInSeconds());

        return response;
    }
}