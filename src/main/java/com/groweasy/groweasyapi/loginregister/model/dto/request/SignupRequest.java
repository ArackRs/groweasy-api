package com.groweasy.groweasyapi.loginregister.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String username,
        @NotBlank
        String password
) {
    public SignupRequest withPasswordEncoded(String password) {
        return new SignupRequest(firstName, lastName, username, password);
    }
    public static SignupRequest of(String firstName, String lastName, String username, String password) {
        return new SignupRequest(firstName, lastName, username, password);
    }
}