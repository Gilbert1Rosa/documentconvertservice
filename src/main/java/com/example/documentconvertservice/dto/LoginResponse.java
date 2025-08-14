package com.example.documentconvertservice.dto;


import java.time.LocalDateTime;


public record LoginResponse(String token, LocalDateTime createdAt, LocalDateTime expiresIn) {

}
