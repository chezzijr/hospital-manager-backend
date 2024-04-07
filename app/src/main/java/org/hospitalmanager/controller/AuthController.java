package org.hospitalmanager.controller;

import org.hospitalmanager.model.User.Role;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.util.MultiValueMap;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpStatus;

import org.hospitalmanager.dto.RefreshTokenResponsePayload;
import org.hospitalmanager.dto.SignInInfo;
import org.hospitalmanager.dto.Auth.*;
import org.hospitalmanager.service.AuthService;
import org.hospitalmanager.service.AuthServiceException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value="/signin", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignResponse> signInUser(@RequestBody SigninRequest req) {
        try {
            SignInInfo signInInfo = authService.signInEmailPassword(req.getEmail(), req.getPassword(), null);
            SignResponse resp = new SignResponse(signInInfo.getIdToken(), signInInfo.getRefreshToken(), signInInfo.isEmailVerified());
            return ResponseEntity.ok(resp);
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value="/signup", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignResponse> signUpUser(@RequestBody SignupRequest req) {
        // cannot create admin user
        if (req.getRole().equals(Role.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot create admin user");
        }

        try {
            SignInInfo signInInfo = authService.signUpEmailPassword(req.getEmail(), req.getPassword(), req.getRole());
            SignResponse resp = new SignResponse(signInInfo.getIdToken(), signInInfo.getRefreshToken(), signInInfo.isEmailVerified());
            return ResponseEntity.ok(resp);
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value="/resetPassword", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestBody ResetPasswordRequest req) {
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
        try {
            RefreshTokenResponsePayload payload = authService.refreshToken(req.getRefreshToken());
            RefreshTokenResponse resp = new RefreshTokenResponse(payload.getIdToken(), payload.getRefreshToken());
            return ResponseEntity.ok(resp);
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value="/updatePassword", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatePasswordResponse> updatePassword(@RequestHeader MultiValueMap<String, String> headers, @RequestBody UpdatePasswordRequest req) {
        String authHeader = headers.getFirst("authorization");

        // remove "Bearer " from the token by splitting the string
        String[] tokens = authHeader.split(" ");
        if (tokens.length != 2 || !tokens[0].equals("Bearer")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String token = tokens[1].trim(); // prevent some goofy ahh trailing CR LF

        try {
            authService.updateUserPassword(token, req.getNewPassword());
            UpdatePasswordResponse resp = new UpdatePasswordResponse("PASSWORD_UPDATED");
            return ResponseEntity.ok(resp);
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
