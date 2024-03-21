package org.hospitalmanager.model;

/**
 * The response payload for a sign-in request when use Firebase REST
 * Because FirebaseAuth admin SDK does not support sign-in, we have to use the REST API
 * Immutable
 */
public class SignInResponsePayload {
    private String idToken;
    private String email;
    private String refreshToken;
    private String expiresIn;
    private String localId;
    private String registered;

    public String getIdToken() {
        return idToken;
    }

    public String getEmail() {
        return email;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getLocalId() {
        return localId;
    }

    public String getRegistered() {
        return registered;
    }
}
