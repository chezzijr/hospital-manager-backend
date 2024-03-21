package org.hospitalmanager.service;

import org.hospitalmanager.model.SignInInfo;
import org.hospitalmanager.repository.AuthRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

public interface AuthService {
    public SignInInfo signInEmailPassword(String email, String password) throws Exception;

    public SignInInfo signUpEmailPassword(String email, String password) throws Exception;

    /**
     * Send a verification email to the user
     * @param user The user to send the email to, non-null
     * @throws Exception if the email could not be sent
     */
    public void sendVerificationEmail(String idToken) throws Exception;

    /**
     * Verify a token
     * @param token The token to verify, non-null and non-empty
     * @return true if the token is valid, false otherwise
     * @throws FirebaseAuthException if the token is invalid
     */
    public FirebaseToken verifyToken(String token) throws FirebaseAuthException;
}

@Service
class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthRepository authRepository;

    @Override
    public SignInInfo signInEmailPassword(String email, String password) throws Exception {
        var payload = authRepository.signInUserEmailPassword(email, password);
        var user = authRepository.getUser(payload.getLocalId());

        String idToken = payload.getIdToken();
        if (!user.isEmailVerified()) {
            sendVerificationEmail(idToken);
        }

        return new SignInInfo(idToken, payload.getRefreshToken(), user.isEmailVerified());
    }

    @Override
    public SignInInfo signUpEmailPassword(String email, String password) throws Exception {
        var user = authRepository.createUserEmailPassword(email, password);
        return signInEmailPassword(email, password);
    }

    @Override
    public void sendVerificationEmail(String idToken) throws Exception {
        authRepository.sendEmailVerification(idToken);
    }

    @Override
    public FirebaseToken verifyToken(String token) throws FirebaseAuthException {
        return authRepository.verifyToken(token);
    }
}
