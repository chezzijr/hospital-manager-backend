package org.hospitalmanager.model;

public class ResetPasswordResponse {
    private String message;

    public ResetPasswordResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
