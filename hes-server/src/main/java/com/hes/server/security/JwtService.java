package com.hes.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final SecretKey key;
    private final long accessTtlSeconds;
    private final long refreshTtlSeconds;

    public JwtService(
            @Value("${hes.security.jwt.secret:change-me-hes-jwt-secret-key-32bytes-min}") String secret,
            @Value("${hes.security.jwt.access-ttl-seconds:900}") long accessTtlSeconds,
            @Value("${hes.security.jwt.refresh-ttl-seconds:604800}") long refreshTtlSeconds) {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("hes.security.jwt.secret must be at least 32 bytes");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTtlSeconds = accessTtlSeconds;
        this.refreshTtlSeconds = refreshTtlSeconds;
    }

    public String createAccessToken(String username, Collection<String> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .claim("roles", List.copyOf(roles))
                .claim("typ", "access")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTtlSeconds)))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .claim("typ", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(refreshTtlSeconds)))
                .signWith(key)
                .compact();
    }

    public JwtPrincipal parseAccessToken(String token) {
        Claims claims = parse(token);
        if (!"access".equals(String.valueOf(claims.get("typ")))) {
            throw new IllegalArgumentException("Not an access token");
        }
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("roles", List.class);
        return new JwtPrincipal(claims.getSubject(), roles == null ? List.of() : roles);
    }

    public String parseRefreshSubject(String token) {
        Claims claims = parse(token);
        if (!"refresh".equals(String.valueOf(claims.get("typ")))) {
            throw new IllegalArgumentException("Not a refresh token");
        }
        return claims.getSubject();
    }

    private Claims parse(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public record JwtPrincipal(String username, List<String> roles) {
    }
}
