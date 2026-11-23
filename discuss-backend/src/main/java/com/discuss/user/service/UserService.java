package com.discuss.user.service;

import java.time.Instant;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.discuss.user.dto.UserRequest;
import com.discuss.user.dto.UserResponse;
import com.discuss.user.enums.AccountStatusEnum;
import com.discuss.user.exception.DuplicateEmailException;
import com.discuss.user.exception.DuplicateUsernameException;
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

        // Normalize input
        String username = request.getUsername()
                .trim()
                .toLowerCase(Locale.ROOT);

        String email = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        // Check for duplicate username
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException("Username is already taken");
        }

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email is already registered");
        }

        // Create User entity
        User user = new User();

        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setAccountStatus(AccountStatusEnum.ACTIVE);
        user.setEmailVerified(false);
        user.setReputation(0);
        user.setCreatedAt(Instant.now());

        // Save user
        User savedUser = userRepository.save(user);

        // Map entity to response
        UserResponse response = new UserResponse();

        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        response.setReputation(savedUser.getReputation());
        response.setCreatedAt(savedUser.getCreatedAt());

        return response;
    }
}
