package org.hospitalmanager.service;

import org.hospitalmanager.model.RefreshTokenResponsePayload;
import org.hospitalmanager.model.SignInInfo;
import org.hospitalmanager.repository.AuthRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.annotations.Nullable;

public interface AuthService {
    /**
     * Sign in a user using their email and password
     * @param email The user's email, non-null and non-empty
     * @param password The user's password, non-null and non-empty
     * @param user The user record, nullable, prevent duplicate calls to the database
     * @return The sign-in response payload
     * @throws Exception if the user does not exist
     */
    public SignInInfo signInEmailPassword(String email, String password, @Nullable UserRecord user) throws Exception;

    /**
     * Sign up a user using their email and password
     * After creating the user, the user will be signed in
     * @param email The user's email, non-null and non-empty
     * @param password The user's password, non-null and non-empty
     * @return The sign-in response payload
     * @throws Exception if the user does not exist
     */
    public SignInInfo signUpEmailPassword(String email, String password) throws Exception;

    /**
     * Send a verification email to the user
     * @param user The user to send the email to, non-null
     * @throws Exception if the email could not be sent
     */
    public void sendVerificationEmail(String idToken) throws Exception;

    /**
     * Verify a token
     * Other controllers should call this method to verify a token before proceeding
     * @param token The token to verify, non-null and non-empty
     * @return true if the token is valid, false otherwise
     * @throws FirebaseAuthException if the token is invalid
     */
    public FirebaseToken verifyToken(String token) throws FirebaseAuthException;

    /**
     * Send a password reset email to the user
     * @param email The user's email, non-null and non-empty
     * @throws Exception if the email could not be sent
     */
    public void sendPasswordResetEmail(String email) throws Exception;

    /**
     * Refresh a token
     * @param refreshToken The refresh token, non-null and non-empty
     * @return The refresh token response payload
     * @throws FirebaseAuthException if the token is invalid
     */
    public RefreshTokenResponsePayload refreshToken(String refreshToken) throws FirebaseAuthException;
}

@Service
class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthRepository authRepository;

    @Override
    public SignInInfo signInEmailPassword(String email, String password, @Nullable UserRecord user) throws Exception {
        var payload = authRepository.signInUserEmailPassword(email, password);
        if (user == null) {
            user = authRepository.getUser(payload.getLocalId());
        }

        String idToken = payload.getIdToken();
        if (!user.isEmailVerified()) {
            sendVerificationEmail(idToken);
        }

        return new SignInInfo(idToken, payload.getRefreshToken(), user.isEmailVerified());
    }

    @Override
    public SignInInfo signUpEmailPassword(String email, String password) throws Exception {
        var user = authRepository.createUserEmailPassword(email, password);
        return signInEmailPassword(email, password, user);
    }

    @Override
    public void sendVerificationEmail(String idToken) throws Exception {
        authRepository.sendEmailVerification(idToken);
    }

    @Override
    public void sendPasswordResetEmail(String email) throws Exception {
        // check if user exists
        authRepository.sendPasswordResetEmail(email);
    }

    @Override
    public FirebaseToken verifyToken(String token) throws FirebaseAuthException {
        return authRepository.verifyToken(token);
    }

    @Override
    public RefreshTokenResponsePayload refreshToken(String refreshToken) throws FirebaseAuthException {
        return authRepository.refreshToken(refreshToken);
    }
}
