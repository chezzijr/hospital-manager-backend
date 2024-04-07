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

    public static class SignResponse {
        private String idToken;
        private String refreshToken;
        private boolean emailVerified;

        public SignResponse(String idToken, String refreshToken, boolean emailVerified) {
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

    public static class RefreshTokenRequest {
        private String refreshToken;

        public String getRefreshToken() {
            return refreshToken;
        }
    }

    public static class RefreshTokenResponse {
        private String accessToken;
        private String refreshToken;

        public RefreshTokenResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
    }

    public static class ResetPasswordRequest {
        private String email;

        public String getEmail() {
            return email;
        }
    }

    public static class ResetPasswordResponse {
        private String message;

        public ResetPasswordResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class UpdatePasswordRequest {
        private String newPassword;

        public String getNewPassword() {
            return newPassword;
        }
    }

    public static class UpdatePasswordResponse {
        private String message;

        public UpdatePasswordResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
