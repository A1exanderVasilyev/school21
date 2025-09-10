package com.catinasw.T03.web.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public class JwtAuthentication implements Authentication {
    private final UUID uuid;
    private final Collection<? extends GrantedAuthority> authorities;
    private boolean isAuthenticated;

    public JwtAuthentication(UUID uuid, Collection<? extends GrantedAuthority> authorities, boolean isAuthenticated) {
        this.uuid = uuid;
        this.authorities = authorities;
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return uuid;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return uuid.toString();
    }
}
