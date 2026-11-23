package com.discuss.auth.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.discuss.user.enums.AccountStatusEnum;
import com.discuss.user.model.User;

public class CustomUserPrincipal implements UserDetails {

    private final String id;
    private final String username;
    private final String password;
    private final AccountStatusEnum accountStatus;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserPrincipal(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPasswordHash();
        this.accountStatus = user.getAccountStatus();

        this.authorities = java.util.List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        );
    }

    public String getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountStatus != AccountStatusEnum.BANNED
                && accountStatus != AccountStatusEnum.SUSPENDED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return accountStatus == AccountStatusEnum.ACTIVE;
    }
}