package org.hospitalmanager.model;

public class UpdatePassword {
    public static class Request {
        private String newPassword;

        public String getNewPassword() {
            return newPassword;
        }
    }

    public static class Response {
        private String message;

        public Response(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
