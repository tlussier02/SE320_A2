package com.dta.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

    private final JwtProperties jwtProperties;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateAccessToken(UUID userId, String email, String role) {
        return buildToken(
                Map.of("role", role, "email", email),
                userId.toString(),
                jwtProperties.getAccessTokenExpiration()
        );
    }

    public String generateRefreshToken(UUID userId) {
        return buildToken(
                Map.of("type", "refresh"),
                userId.toString(),
                jwtProperties.getRefreshTokenExpiration()
        );
    }

    public boolean validate(String token) {
        return extractAllClaims(token).getExpiration().toInstant().isAfter(Instant.now());
    }

    public String getSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String buildToken(Map<String, ?> claims, String subject, long expirationMillis) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMillis)))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return (SecretKey) key;
    }
}
