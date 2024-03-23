package org.hospitalmanager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.api.client.auth.oauth2.RefreshTokenRequest;

import org.springframework.util.MultiValueMap;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

import org.hospitalmanager.dto.RefreshTokenResponsePayload;
import org.hospitalmanager.dto.SignInInfo;
import org.hospitalmanager.model.RefreshTokenResponse;
import org.hospitalmanager.model.ResetPasswordRequest;
import org.hospitalmanager.model.ResetPasswordResponse;
import org.hospitalmanager.model.SignRequest;
import org.hospitalmanager.model.SignResponse;
import org.hospitalmanager.service.AuthService;
import org.hospitalmanager.service.AuthServiceException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value="/signin", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignResponse> signInUser(@RequestBody SignRequest req) {
        if (req.getEmail() == null || req.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
        }

        try {
            SignInInfo signInInfo = authService.signInEmailPassword(req.getEmail(), req.getPassword(), null);
            SignResponse resp = new SignResponse(signInInfo.getIdToken(), signInInfo.getRefreshToken(), signInInfo.isEmailVerified());
            return ResponseEntity.ok(resp);
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value="/signup", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignResponse> signUpUser(@RequestBody SignRequest req) {
        if (req.getEmail() == null || req.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
        }

        try {
            SignInInfo signInInfo = authService.signUpEmailPassword(req.getEmail(), req.getPassword());
            SignResponse resp = new SignResponse(signInInfo.getIdToken(), signInInfo.getRefreshToken(), signInInfo.isEmailVerified());
            return ResponseEntity.ok(resp);
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String token = tokens[1].trim(); // prevent some goofy ahh trailing CR LF

        HashMap<String, String> response = new HashMap<String, String>();
        try {
            response.put("email", authService.verifyToken(token).getEmail());
            return response;
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value="/resetPassword", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestBody ResetPasswordRequest req) {
        if (req.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }

        try {
            authService.sendPasswordResetEmail(req.getEmail());
            ResetPasswordResponse resp = new ResetPasswordResponse("Password reset email sent");
            return ResponseEntity.ok(resp);
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value="/refreshToken", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest req) {
        if (req.getRefreshToken() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token is required");
        }

        try {
            RefreshTokenResponsePayload payload = authService.refreshToken(req.getRefreshToken());
            RefreshTokenResponse resp = new RefreshTokenResponse(payload.getIdToken(), payload.getRefreshToken());
            return ResponseEntity.ok(resp);
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
