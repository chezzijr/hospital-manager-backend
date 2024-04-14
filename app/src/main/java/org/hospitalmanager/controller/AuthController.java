package org.hospitalmanager.controller;

import org.hospitalmanager.model.User.Role;
import org.hospitalmanager.util.AuthorizationUtil;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.firebase.auth.FirebaseToken;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

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

    @Autowired
    private AuthorizationUtil authUtil;

    @PostMapping(value="/signin", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignResponse> signInUser(@RequestBody SigninRequest req) {
        try {
            SignInInfo signInInfo = authService.signInEmailPassword(req.getEmail(), req.getPassword(), null);
            SignResponse resp = new SignResponse(signInInfo.getUid(), signInInfo.getRole(), signInInfo.getIdToken(), signInInfo.getRefreshToken(), signInInfo.isEmailVerified());
            return ResponseEntity.ok(resp);
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // this is used for PATIENT only
    @PostMapping(value="/signup", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignResponse> signUpUser(@RequestBody SignupRequest req) {
        // cannot create admin user
        if (!req.getRole().equals(Role.PATIENT)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot create admin user");
        }

        try {
            SignInInfo signInInfo = authService.signUpEmailPassword(req.getEmail(), req.getPassword(), req.getRole());
            SignResponse resp = new SignResponse(signInInfo.getUid(), signInInfo.getRole(), signInInfo.getIdToken(), signInInfo.getRefreshToken(), signInInfo.isEmailVerified());
            return ResponseEntity.ok(resp);
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // used for admin to create user with role DOCTOR or NURSE
    @PostMapping(value="/createUser", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignResponse> createUser(@RequestHeader HashMap<String, String> headers, @RequestBody SignupRequest req) {
        FirebaseToken token = authUtil.isAuthorized(headers.get("authorization"), Role.ADMIN);
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        try {
            SignInInfo signInInfo = authService.signUpEmailPassword(req.getEmail(), req.getPassword(), req.getRole());
            SignResponse resp = new SignResponse(signInInfo.getUid(), signInInfo.getRole(), signInInfo.getIdToken(), signInInfo.getRefreshToken(), signInInfo.isEmailVerified());
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
    public ResponseEntity<UpdatePasswordResponse> updatePassword(@RequestHeader HashMap<String, String> headers, @RequestBody UpdatePasswordRequest req) {
        FirebaseToken token = authUtil.isAuthorized(headers.get("authorization"), Role.PATIENT, Role.NURSE, Role.DOCTOR, Role.ADMIN);
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        try {
            authService.updateUserPassword(token.getUid(), req.getNewPassword());
            UpdatePasswordResponse resp = new UpdatePasswordResponse("PASSWORD_UPDATED");
            return ResponseEntity.ok(resp);
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
