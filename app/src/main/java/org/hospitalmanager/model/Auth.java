package org.hospitalmanager.model;

public class Auth {
    public static class Request {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
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
