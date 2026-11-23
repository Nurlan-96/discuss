package com.discuss.auth.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.discuss.user.model.User;
import com.discuss.user.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) {

        User user = userRepository
                .findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return new CustomUserPrincipal(user);
    }

    public CustomUserPrincipal loadUserById(String id) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return new CustomUserPrincipal(user);
    }
}