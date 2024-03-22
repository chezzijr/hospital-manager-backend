package org.hospitalmanager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.util.MultiValueMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

import org.hospitalmanager.model.RefreshTokenResponsePayload;
import org.hospitalmanager.model.SignInInfo;
import org.hospitalmanager.service.AuthService;

@ResponseStatus(code=HttpStatus.UNAUTHORIZED, reason="You are not authorized to access this resource")
class UnauthorizedException extends RuntimeException {
    public UnauthorizedException (String message) {
        super(message);
    }
}

@ResponseStatus(code=HttpStatus.BAD_REQUEST, reason="Invalid credentials")
class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException (String message) {
        super(message);
    }
}

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value="/signin", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, Object> getUser(@RequestBody HashMap<String, String> formData) {
        String email = formData.get("email");
        String password = formData.get("password");
        HashMap<String, Object> response = new HashMap<>();
        try {
            SignInInfo signInInfo = authService.signInEmailPassword(email, password);
            response.put("token", signInInfo.getIdToken());
            response.put("refreshToken", signInInfo.getRefreshToken());
            response.put("emailVerified", signInInfo.isEmailVerified());
            return response;
        } catch (Exception e) {
            throw new InvalidCredentialsException(e.getMessage());
        }
    }

    @PostMapping(value="/signup", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, Object> registerUser(@RequestBody HashMap<String, String> formData) {
        String email = formData.get("email");
        String password = formData.get("password");
        HashMap<String, Object> response = new HashMap<>();
        try {
            SignInInfo signInInfo = authService.signUpEmailPassword(email, password);
            response.put("token", signInInfo.getIdToken());
            response.put("refreshToken", signInInfo.getRefreshToken());
            response.put("emailVerified", signInInfo.isEmailVerified());
            return response;
        } catch (Exception e) {
            throw new InvalidCredentialsException(e.getMessage());
        }
    }

    // get user info, using token in header Authorization
    // for testing only
    @GetMapping(value="/user", produces=MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, String> getUser(@RequestHeader MultiValueMap<String, String> headers) {
        System.out.println(headers);

        String authHeader = headers.getFirst("authorization");

        // remove "Bearer " from the token by splitting the string
        String[] tokens = authHeader.split(" ");
        if (tokens.length != 2 || !tokens[0].equals("Bearer")) {
            throw new UnauthorizedException("Invalid token");
        }

        String token = tokens[1].trim(); // prevent some goofy ahh trailing CR LF

        HashMap<String, String> response = new HashMap<String, String>();
        try {
            response.put("email", authService.verifyToken(token).getEmail());
            return response;
        } catch (Exception e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

    @PostMapping(value="/resetPassword", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, String> resetPassword(@RequestBody HashMap<String, String> formData) {
        String email = formData.get("email");
        try {
            authService.sendPasswordResetEmail(email);
            HashMap<String, String> response = new HashMap<>();
            response.put("message", "Password reset email sent");
            return response;
        } catch (Exception e) {
            throw new InvalidCredentialsException(e.getMessage());
        }
    }

    @PostMapping(value="/refreshToken", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, String> refreshToken(@RequestBody HashMap<String, String> formData) {
        String refreshToken = formData.get("refreshToken");
        try {
            RefreshTokenResponsePayload payload = authService.refreshToken(refreshToken);
            HashMap<String, String> response = new HashMap<>();
            response.put("token", payload.getIdToken());
            response.put("refreshToken", payload.getRefreshToken());
            return response;
        } catch (Exception e) {
            throw new InvalidCredentialsException(e.getMessage());
        }
    }
}
