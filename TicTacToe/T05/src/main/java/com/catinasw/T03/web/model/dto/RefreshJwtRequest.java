package com.catinasw.T03.web.model.dto;

public class RefreshJwtRequest {
    private String refreshToken;

    public RefreshJwtRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
