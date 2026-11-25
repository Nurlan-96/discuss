package com.discuss.user.service;

import java.time.Instant;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.discuss.user.dto.UpdateProfileRequest;
import com.discuss.user.dto.UserRequest;
import com.discuss.user.dto.UserResponse;
import com.discuss.user.enums.AccountStatusEnum;
import com.discuss.user.exception.DuplicateEmailException;
import com.discuss.user.exception.DuplicateUsernameException;
import com.discuss.user.exception.UserNotFoundException;
import com.discuss.user.model.User;
import com.discuss.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(UserRequest request) {

        String username = request.getUsername()
                .trim()
                .toLowerCase(Locale.ROOT);

        String email = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException("Username is already taken");
        }

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email is already registered");
        }

        User user = new User();

        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setAccountStatus(AccountStatusEnum.ACTIVE);
        user.setEmailVerified(false);
        user.setReputation(0);
        user.setCreatedAt(Instant.now());

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    public UserResponse getProfile(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()
                        -> new UserNotFoundException("User not found"));

        return mapToResponse(user);
    }

    private UserResponse mapToResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setBio(user.getBio());
        response.setReputation(user.getReputation());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }

public UserResponse updateProfile(
        String userId,
        UpdateProfileRequest request) {

    User user = userRepository.findById(userId)
            .orElseThrow(() ->
                    new UserNotFoundException("User not found"));

    boolean changed = false;

    if (request.getUsername() != null) {

        String username = request.getUsername()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (!username.equals(user.getUsername())) {

            if (userRepository.existsByUsername(username)) {
                throw new DuplicateUsernameException(
                        "Username is already taken");
            }

            user.setUsername(username);
            changed = true;
        }
    }

    if (request.getBio() != null) {

        String bio = request.getBio().trim();
        String normalizedBio = bio.isEmpty() ? null : bio;

        if (!java.util.Objects.equals(
                normalizedBio, user.getBio())) {

            user.setBio(normalizedBio);
            changed = true;
        }
    }

    if (changed) {
        user = userRepository.save(user);
    }

    return mapToResponse(user);
}
}