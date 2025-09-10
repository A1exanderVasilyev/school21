package com.catinasw.T03.web.filter;

import com.catinasw.T03.security.JwtProvider;
import com.catinasw.T03.security.JwtUtil;
import com.catinasw.T03.web.model.JwtAuthentication;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.UUID;

@Component
public class AuthFilter extends GenericFilterBean {
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthFilter(JwtProvider jwtProvider, JwtUtil jwtUtil) {
        this.jwtProvider = jwtProvider;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        if (httpRequest.getRequestURI().startsWith("/auth") && !httpRequest.getRequestURI().equals("/auth/update-refresh-token")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);
        if (!jwtProvider.isAccessTokenValid(token)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            Claims claims = jwtProvider.retrieveClaims(token);
            UUID uuid = UUID.fromString(claims.get("uuid", String.class));
            JwtAuthentication jwtAuthentication = jwtUtil.createJwtAuth(claims);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
            httpRequest.setAttribute("creatorId", uuid);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
