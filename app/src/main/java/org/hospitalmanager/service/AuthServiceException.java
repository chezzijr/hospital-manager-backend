package org.hospitalmanager.service;

public class AuthServiceException extends Exception {
    Throwable cause;

    public AuthServiceException(String message) {
        super(message);
    }

    public AuthServiceException(String message, Exception cause) {
        super(message);
        this.cause = cause;
    }
}
