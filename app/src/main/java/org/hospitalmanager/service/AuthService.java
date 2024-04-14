package org.hospitalmanager.service;

import org.hospitalmanager.dto.RefreshTokenResponsePayload;
import org.hospitalmanager.dto.SignInInfo;
import org.hospitalmanager.service.AuthServiceException;
import org.hospitalmanager.repository.AuthRepositoryException.*;
import org.hospitalmanager.repository.AuthRepository;
import org.hospitalmanager.model.User.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.UpdateRequest;
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
    public SignInInfo signInEmailPassword(String email, String password, @Nullable UserRecord user) throws AuthServiceException;

    /**
     * Sign up a user using their email and password
     * After creating the user, the user will be signed in
     * @param email The user's email, non-null and non-empty
     * @param password The user's password, non-null and non-empty
     * @return The sign-in response payload
     * @throws Exception if the user does not exist
     */
    public SignInInfo signUpEmailPassword(String email, String password, Role role) throws AuthServiceException;

    /**
     * Send a verification email to the user
     * @param user The user to send the email to, non-null
     * @throws Exception if the email could not be sent
     */
    public void sendVerificationEmail(String idToken) throws AuthServiceException;

    /**
     * Verify a token
     * Other controllers should call this method to verify a token before proceeding
     * @param token The token to verify, non-null and non-empty
     * @return true if the token is valid, false otherwise
     * @throws FirebaseAuthException if the token is invalid
     */
    public FirebaseToken verifyToken(String token) throws AuthServiceException;

    /**
     * Send a password reset email to the user
     * @param email The user's email, non-null and non-empty
     * @throws Exception if the email could not be sent
     */
    public void sendPasswordResetEmail(String email) throws AuthServiceException;

    /**
     * Refresh a token
     * @param refreshToken The refresh token, non-null and non-empty
     * @return The refresh token response payload
     * @throws FirebaseAuthException if the token is invalid
     */
    public RefreshTokenResponsePayload refreshToken(String refreshToken) throws AuthServiceException;

    public UserRecord updateUserPassword(String idToken, String password) throws AuthServiceException;
}

@Service
class AuthServiceImpl implements AuthService {

    Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private AuthRepository authRepository;

    @Override
    public SignInInfo signInEmailPassword(String email, String password, @Nullable UserRecord user) throws AuthServiceException {
        try {
            var payload = authRepository.signInUserEmailPassword(email, password);
            if (user == null) {
                user = authRepository.getUser(payload.getLocalId());
            }

            String idToken = payload.getIdToken();
            if (!user.isEmailVerified()) {
                sendVerificationEmail(idToken);
            }

            Role r = switch (user.getDisplayName()) {
                case "PATIENT" -> Role.PATIENT;
                case "DOCTOR" -> Role.DOCTOR;
                case "ADMIN" -> Role.ADMIN;
                default -> {
                    logger.warn("Unknown role: " + user.getDisplayName());
                    throw new AuthServiceException("UNKNOWN_ROLE");
                }
            };

            return new SignInInfo(user.getUid(), r, idToken, payload.getRefreshToken(), user.isEmailVerified());
        } catch (UserNotFoundException e)  {
            throw new AuthServiceException("USER_NOT_FOUND", e);
        }
    }

    @Override
    public SignInInfo signUpEmailPassword(String email, String password, Role role) throws AuthServiceException {
        try {
            var user = authRepository.createUserEmailPassword(email, password, role);
            return signInEmailPassword(email, password, user);
        } catch (UserAlreadyExistsException e) {
            throw new AuthServiceException("USER_ALREADY_EXISTS", e);
        }
    }

    @Override
    public void sendVerificationEmail(String idToken) throws AuthServiceException {
        try {
            authRepository.sendEmailVerification(idToken);
        } catch (UserNotFoundException e) {
            logger.warn("Impossible exception thrown: ", e.getClass().getName());
            throw new AuthServiceException("UNKNOWN_ERROR", e);
        }
    }

    @Override
    public void sendPasswordResetEmail(String email) throws AuthServiceException {
        // check if user exists
        try {
            authRepository.getUserByEmail(email);
        } catch (UserNotFoundException e) {
            throw new AuthServiceException("USER_NOT_FOUND", e);
        }
    }

    @Override
    public FirebaseToken verifyToken(String token) throws AuthServiceException {
        try {
            return authRepository.verifyToken(token);
        } catch (InvalidTokenException e) {
            throw new AuthServiceException("INVALID_TOKEN", e);
        }
    }

    @Override
    public RefreshTokenResponsePayload refreshToken(String refreshToken) throws AuthServiceException {
        try {
            return authRepository.refreshToken(refreshToken);
        } catch (InvalidTokenException e) {
            throw new AuthServiceException("INVALID_TOKEN", e);
        }
    }

    @Override
    public UserRecord updateUserPassword(String idToken, String password) throws AuthServiceException {
        FirebaseToken token = verifyToken(idToken);
        UpdateRequest req = new UpdateRequest(token.getUid()).setPassword(password);
        try {
            return authRepository.updateUser(req);
        } catch (UserNotFoundException e) {
            logger.warn("Impossible exception thrown: ", e.getClass().getName());
            throw new AuthServiceException("USER_NOT_FOUND", e);
        }
    }
}
