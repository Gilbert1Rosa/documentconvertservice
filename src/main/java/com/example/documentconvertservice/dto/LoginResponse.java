package com.example.documentconvertservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LoginResponse {

    private final String token;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresIn;

    public LoginResponse(String token, LocalDateTime createdAt, LocalDateTime expiresIn) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiresIn() {
        return expiresIn;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
