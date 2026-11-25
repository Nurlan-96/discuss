package com.discuss.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {

    @Size(min = 3, max = 30,
          message = "Username must be between 3 and 30 characters")
    @Pattern(
        regexp = ".*\\S.*",
        message = "Username cannot contain only whitespace"
    )
    private String username;

    @Size(max = 300,
          message = "Bio cannot exceed 300 characters")
    private String bio;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}