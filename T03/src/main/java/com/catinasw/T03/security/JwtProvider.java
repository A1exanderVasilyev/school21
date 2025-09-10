package com.catinasw.T03.security;

import com.catinasw.T03.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    @Value("${jwt_secret}")
    private String secret;
    private final String issuer = "catinasw";

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", user.getUuid());
        claims.put("roles", user.getRoles());
        int expirationMinutes = 60;
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(expirationMinutes).toInstant());
        return Jwts.builder()
                .claims(claims)
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", user.getUuid());
        int expirationDays = 7;
        Date expirationDate = Date.from(ZonedDateTime.now().plusDays(expirationDays).toInstant());
        return Jwts.builder()
                .claims(claims)
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isRefreshTokenValid(String token) {
        if (token == null) {
            return false;
        }
        try {
            Claims claims = retrieveClaims(token);
            if (isTokenExpired(claims)) {
                return false;
            }

            return claims.get("uuid") != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAccessTokenValid(String token) {
        if (token == null) {
            return false;
        }
        try {
            Claims claims = retrieveClaims(token);
            if (isTokenExpired(claims)) {
                return false;
            }
            return claims.get("uuid") != null && claims.get("roles") != null;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public Claims retrieveClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public List<String> getValidatedRoles(Object rolesClaim) {
        if (rolesClaim == null) {
            return Collections.emptyList();
        }
        if (!(rolesClaim instanceof List<?> rawList)) {
            throw new SecurityException("Roles claim not valid");
        }
        return rawList.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .collect(Collectors.toList());
    }

    public UUID getValidatedUUID(Claims tokenClaims) {
        String uuidString = tokenClaims.get("uuid", String.class);
        if (uuidString == null) {
            throw new SecurityException("Missing uuid in token");
        }
        return UUID.fromString(uuidString);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String validationHandlerOfAuthHeaderWithAccessToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        String token = authHeader.substring(7);
        if (!isAccessTokenValid(token)) {
            throw new IllegalArgumentException("Invalid token");
        }
        return token;
    }
}
