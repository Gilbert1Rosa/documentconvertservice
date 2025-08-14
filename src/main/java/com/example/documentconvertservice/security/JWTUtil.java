package com.example.documentconvertservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String generate(String username, Integer ttl) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ttl))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) throws JwtException {
        return extractClaim(token, Claims::getSubject);
    }

    public LocalDateTime extractExpirationDate(String token) throws JwtException {
        Date issuedAt = extractClaim(token, Claims::getExpiration);
        return issuedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public LocalDateTime extractCreatedAt(String token) throws JwtException {
        Date issuedAt = extractClaim(token, Claims::getIssuedAt);
        return issuedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public boolean isTokenValid(String token, String username) {
        return !isTokenExpired(token) && extractUsername(token).equals(username);
    }

    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).isBefore(LocalDateTime.now());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws JwtException {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractClaims(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
