package org.hospitalmanager.model;


public class User {

    public static enum Role {
        ADMIN, DOCTOR, NURSE, PATIENT
    }

    private String id;
    private String email;
    private Role role;

    public User(String id, String email, Role role, String password) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
