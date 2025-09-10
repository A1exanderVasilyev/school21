package com.catinasw.T03.security;

import com.catinasw.T03.web.model.JwtAuthentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.RequiredTypeException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private final JwtProvider jwtProvider;

    public JwtUtil(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public JwtAuthentication createJwtAuth(Claims tokenClaims) {
        if (tokenClaims == null) {
            throw new IllegalArgumentException("claims is empty");
        }

        try {
            UUID uuid = jwtProvider.getValidatedUUID(tokenClaims);
            List<String> roles = jwtProvider.getValidatedRoles(tokenClaims.get("roles"));
            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return new JwtAuthentication(uuid, authorities, true);
        } catch (RequiredTypeException | SecurityException e) {
            throw new SecurityException(e.getMessage());
        }
    }
}
