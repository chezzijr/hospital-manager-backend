package org.hospitalmanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The payload of a refresh token response
 * Firebase changed the API response, instead of camelCase, it is now snake_case
 * But they did not update the documentation
 * So we have to manually map the response to the object
 * The other responses are still in camelCase
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshTokenResponsePayload {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private String expiresIn;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("project_id")
    private String projectId;

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getExpiresIn() {
        return this.expiresIn;
    }

    public String getTokenType() {
        return this.tokenType;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public String getIdToken() {
        return this.idToken;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getProjectId() {
        return this.projectId;
    }
}
