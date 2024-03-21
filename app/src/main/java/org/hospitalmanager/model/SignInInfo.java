package org.hospitalmanager.model;

/**
 * The response payload for a sign-in request
 * Immutable
 */
public class SignInInfo {
    private String idToken;
    private String refreshToken;
    private boolean emailVerified;

    public SignInInfo(String idToken, String refreshToken, boolean emailVerified) {
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        this.emailVerified = emailVerified;
    }

    public String getIdToken() {
        return idToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }
}
