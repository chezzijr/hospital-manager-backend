package org.hospitalmanager.model;

// Used for both sign-in and sign-up responses
public class SignResponse {
    private String accessToken;
    private String refreshToken;
    private boolean emailVerified;

    public SignResponse(String accessToken, String refreshToken, boolean emailVerified) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.emailVerified = emailVerified;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }
}
