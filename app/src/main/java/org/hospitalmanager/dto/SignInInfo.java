package org.hospitalmanager.dto;

import org.hospitalmanager.model.User.Role;
/**
 * The response payload for a sign-in request
 * Immutable
 */
public class SignInInfo {
    private String uid;
    private Role role;
    private String idToken;
    private String refreshToken;
    private boolean emailVerified;

    public SignInInfo(String uid, Role role, String idToken, String refreshToken, boolean emailVerified) {
        this.uid = uid;
        this.role = role;
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        this.emailVerified = emailVerified;
    }

    public String getUid() {
        return uid;
    }

    public Role getRole() {
        return role;
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
