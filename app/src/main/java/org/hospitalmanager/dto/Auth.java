package org.hospitalmanager.dto;

import org.hospitalmanager.model.User.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Auth {
    public static class SigninRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Email is not valid")
        private String email;
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        private String password;

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class SignupRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Email is not valid")
        private String email;
        @NotBlank(message = "Password is required")
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
        @NotBlank(message = "Refresh token is required")
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
        @NotBlank(message = "Email is required")
        @Email(message = "Email is not valid")
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
        @NotBlank(message = "New password is required")
        @Size(min = 6, message = "Password must be at least 6 characters long")
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
