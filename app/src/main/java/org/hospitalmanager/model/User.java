package org.hospitalmanager.model;


public class User {

    public static enum Role {
        ADMIN, DOCTOR, NURSE, PATIENT
    }

    private String id;
    private String email;
    private Role role;
    private String password;

    public User(String id, String email, Role role, String password) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.password = password;
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

    public String getPassword() {
        return password;
    }
}
