package com.acadance.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms:604800000}") // default 7 days
    private long expirationMs;

    private SecretKey key;

    private SecretKey getKey() {
        if (key == null) {
            key = Keys.hmacShaKeyFor(secret.getBytes());
        }
        return key;
    }

    public String generateToken(Long userId, String name) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("name", name)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long extractUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.valueOf(claims.getSubject());
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
