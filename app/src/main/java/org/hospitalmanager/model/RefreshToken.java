package org.hospitalmanager.model;

public class RefreshToken {
    public static class Request {
        private String refreshToken;

        public String getRefreshToken() {
            return refreshToken;
        }
    }

    public static class Response {
        private String accessToken;
        private String refreshToken;

        public Response(String accessToken, String refreshToken) {
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
}
