package com.discuss.user.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.discuss.auth.security.CustomUserPrincipal;
import com.discuss.user.dto.UpdateProfileRequest;
import com.discuss.user.dto.UserResponse;
import com.discuss.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class MeController {

    private final UserService userService;

    public MeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser(Authentication authentication) {

        CustomUserPrincipal principal
                = (CustomUserPrincipal) authentication.getPrincipal();

        return userService.getProfile(principal.getId());
    }

    @PatchMapping("/me")
    public UserResponse updateCurrentUser(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {

        CustomUserPrincipal principal
                = (CustomUserPrincipal) authentication.getPrincipal();

        return userService.updateProfile(
                principal.getId(),
                request
        );
    }
}
