package com.discuss.user.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.discuss.auth.security.CustomUserPrincipal;

@RestController
@RequestMapping("/api/v1/users")
public class MeController {

    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(
            Authentication authentication) {

        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        return Map.of(
                "id", principal.getId(),
                "username", principal.getUsername(),
                "authenticated", authentication.isAuthenticated()
        );
    }
}