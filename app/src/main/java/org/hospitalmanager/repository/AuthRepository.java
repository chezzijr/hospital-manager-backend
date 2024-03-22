package org.hospitalmanager.repository;

import java.util.Map;

import org.hospitalmanager.config.Firebase;
import org.hospitalmanager.model.RefreshTokenResponsePayload;
import org.hospitalmanager.model.SignInResponsePayload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import com.google.firebase.auth.UserRecord.CreateRequest;

public interface AuthRepository {
    /**
     * Get a user by their ID
     * 
     * @param id The user's ID, non-null and non-empty
     * @return The user's record
     * @throws FirebaseAuthException if the user does not exist
     */
    UserRecord getUser(String id) throws FirebaseAuthException;

    /**
     * Sign in a user using their email and password
     * 
     * @param email    The user's email, non-null and non-empty
     * @param password The user's password, non-null and non-empty
     * @return The sign-in response payload
     * @throws Exception if the user does not exist
     */
    SignInResponsePayload signInUserEmailPassword(String email, String password) throws Exception;

    /**
     * Create a new user
     * 
     * @param email    The user's email, non-null and non-empty
     * @param password The user's password, non-null and non-empty
     * @return The user's record
     * @throws FirebaseAuthException if the user already exists
     */
    UserRecord createUserEmailPassword(String email, String password) throws FirebaseAuthException;

    /**
     * Create a new user via CreateRequest builder
     * 
     * @param req The user's request, non-null
     * @return The user's record
     * @throws FirebaseAuthException if the user already exists
     */
    UserRecord createUser(CreateRequest req) throws FirebaseAuthException;

    /**
     * Update a user, using UpdateRequest builder
     * 
     * @param req The user's request, non-null
     * @return The user's record
     * @throws FirebaseAuthException if the user does not exist
     */
    UserRecord updateUser(UpdateRequest req) throws FirebaseAuthException;

    /**
     * Generate an email verification link and send it to the user's email
     * 
     * @param email The user's email, non-null and non-empty
     * @return The email verification link
     * @throws FirebaseAuthException if the user does not exist
     */
    String sendEmailVerification(String idToken) throws Exception;

    /**
     * Verify idToken
     * 
     * @param token The token to verify, non-null and non-empty
     * @return The token's payload
     * @throws FirebaseAuthException if the token is invalid
     */
    FirebaseToken verifyToken(String token) throws FirebaseAuthException;

    /**
     * Send a password reset email to the user
     * 
     * @param email The user's email, non-null and non-empty
     * @return The user's email
     * @throws FirebaseAuthException if the user does not exist
     */
    String sendPasswordResetEmail(String email) throws Exception;

    /**
     * Refresh the token
     * 
     * @param refreshToken The refresh token, non-null and non-empty
     * @return The refresh token response payload
     */
    RefreshTokenResponsePayload refreshToken(String refreshToken);
}

@Repository
class AuthRepositoryImpl implements AuthRepository {
    @Autowired
    private Firebase firebase;

    @Value("${firebase.web.api.key}")
    private String apiKey;

    private static final String SIGNIN_PASSWORD_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=";
    private static final String VERIFY_EMAIL_URL = "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key=";
    private static final String PASSWORD_RESET_URL = "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key=";
    private static final String REFRESH_TOKEN_URL = "https://securetoken.googleapis.com/v1/token?key=";

    @Override
    public UserRecord getUser(String id) throws FirebaseAuthException {
        return firebase.getAuth().getUser(id);
    }

    @Override
    public SignInResponsePayload signInUserEmailPassword(String email, String password) throws Exception {
        // Since firebase admin sdk does not have a method to verify a user's password,
        // we have to use the REST API
        WebClient webClient = WebClient.create();
        SignInResponsePayload resp = webClient
                .post()
                .uri(SIGNIN_PASSWORD_URL + apiKey)
                .bodyValue(Map.of("email", email, "password", password, "returnSecureToken", true))
                .retrieve()
                .bodyToMono(SignInResponsePayload.class)
                .block();
        return resp;
    }

    @Override
    public UserRecord createUserEmailPassword(String email, String password) throws FirebaseAuthException {
        CreateRequest req = new CreateRequest()
                .setEmail(email)
                .setPassword(password);
        UserRecord usr = firebase.getAuth().createUser(req);
        return usr;
    }

    @Override
    public UserRecord createUser(CreateRequest req) throws FirebaseAuthException {
        UserRecord usr = firebase.getAuth().createUser(req);
        return usr;
    }

    @Override
    public UserRecord updateUser(UpdateRequest req) throws FirebaseAuthException {
        UserRecord usr = firebase.getAuth().updateUser(req);
        return usr;
    }

    @Override
    public String sendEmailVerification(String idToken) throws Exception {
        // Since firebase admin sdk does not have a method to send an email verification
        // link, we have to use the REST API
        WebClient webClient = WebClient.create();
        // response is an object with field email
        var resp = webClient
                .post()
                .uri(VERIFY_EMAIL_URL + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("requestType", "VERIFY_EMAIL", "idToken", idToken))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String email = resp.get("email").toString();
        return email;
    }

    @Override
    public FirebaseToken verifyToken(String idToken) throws FirebaseAuthException {
        FirebaseToken token = firebase.getAuth().verifyIdToken(idToken);
        return token;
    }

    @Override
    public String sendPasswordResetEmail(String email) throws Exception {
        // Since firebase admin sdk does not have a method to send a password reset
        // email,
        // we have to use the REST API
        WebClient webClient = WebClient.create();
        // response is an object with field email
        var resp = webClient
                .post()
                .uri(PASSWORD_RESET_URL + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("requestType", "PASSWORD_RESET", "email", email))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        return resp.get("email").toString();
    }

    @Override
    public RefreshTokenResponsePayload refreshToken(String refreshToken) {
        WebClient webClient = WebClient.create();

        RefreshTokenResponsePayload resp = webClient
                .post()
                .uri(REFRESH_TOKEN_URL + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("grant_type", "refresh_token", "refresh_token", refreshToken))
                .retrieve()
                .bodyToMono(RefreshTokenResponsePayload.class)
                .block();

        return resp;
    }
}
