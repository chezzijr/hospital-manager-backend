package org.hospitalmanager.model;

// Used for both sign-in and sign-up requests
public class SignRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
