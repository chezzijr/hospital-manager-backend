package org.hospitalmanager.model;

public class ResetPassword {
    public static class Request {
        private String email;

        public String getEmail() {
            return email;
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
