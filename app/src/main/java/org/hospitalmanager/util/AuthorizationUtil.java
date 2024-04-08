package org.hospitalmanager.util;

import org.hospitalmanager.model.User.Role;
import org.hospitalmanager.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.firebase.auth.FirebaseToken;

@Component
public class AuthorizationUtil {
    @Autowired
    private AuthService authService;

    public FirebaseToken isAuthorized(String token, Role ...roles) {
        // Bearer token
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        token = token.substring(7);
        try {
            var firebaseToken = authService.verifyToken(token);
            for (Role role : roles) {
                if (firebaseToken.getName().equals(role.name())) {
                    return firebaseToken;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
