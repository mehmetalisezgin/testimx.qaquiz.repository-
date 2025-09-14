package com.testimx.qaquiz.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Login request payload.  Either username or email may be used as
 * the identifier; for simplicity the field is called `username`.
 */
public class SigninRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}