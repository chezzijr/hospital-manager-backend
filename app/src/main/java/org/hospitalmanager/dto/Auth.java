package org.hospitalmanager.dto;

import org.hospitalmanager.model.User.Role;

public class Auth {
    public static class SigninRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class SignupRequest {
        private String email;
        private String password;
        private Role role;

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public Role getRole() {
            return role;
        }
    }

    public static class Response {
        private String idToken;
        private String refreshToken;
        private boolean emailVerified;

        public Response(String idToken, String refreshToken, boolean emailVerified) {
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
}
