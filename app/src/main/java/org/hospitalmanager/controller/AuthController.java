package org.hospitalmanager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
import org.hospitalmanager.model.RefreshToken;
import org.hospitalmanager.model.ResetPassword;
import org.hospitalmanager.model.UpdatePassword;
import org.hospitalmanager.model.Auth;
import org.hospitalmanager.service.AuthService;
import org.hospitalmanager.service.AuthServiceException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value="/signin", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Auth.Response> signInUser(@RequestBody Auth.Request req) {
        if (req.getEmail() == null || req.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
        }

        try {
            SignInInfo signInInfo = authService.signInEmailPassword(req.getEmail(), req.getPassword(), null);
            Auth.Response resp = new Auth.Response(signInInfo.getIdToken(), signInInfo.getRefreshToken(), signInInfo.isEmailVerified());
            return ResponseEntity.ok(resp);
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value="/signup", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Auth.Response> signUpUser(@RequestBody Auth.Request req) {
        if (req.getEmail() == null || req.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
        }

        try {
            SignInInfo signInInfo = authService.signUpEmailPassword(req.getEmail(), req.getPassword());
            Auth.Response resp = new Auth.Response(signInInfo.getIdToken(), signInInfo.getRefreshToken(), signInInfo.isEmailVerified());
            return ResponseEntity.ok(resp);
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value="/resetPassword", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResetPassword.Response> resetPassword(@RequestBody ResetPassword.Request req) {
        if (req.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }

        try {
            authService.sendPasswordResetEmail(req.getEmail());
            ResetPassword.Response resp = new ResetPassword.Response("Password reset email sent");
            return ResponseEntity.ok(resp);
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value="/refreshToken", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RefreshToken.Response> refreshToken(@RequestBody RefreshToken.Request req) {
        if (req.getRefreshToken() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token is required");
        }

        try {
            RefreshTokenResponsePayload payload = authService.refreshToken(req.getRefreshToken());
            RefreshToken.Response resp = new RefreshToken.Response(payload.getIdToken(), payload.getRefreshToken());
            return ResponseEntity.ok(resp);
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value="/updatePassword", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatePassword.Response> updatePassword(@RequestHeader MultiValueMap<String, String> headers, @RequestBody UpdatePassword.Request req) {
        String authHeader = headers.getFirst("authorization");

        // remove "Bearer " from the token by splitting the string
        String[] tokens = authHeader.split(" ");
        if (tokens.length != 2 || !tokens[0].equals("Bearer")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String token = tokens[1].trim(); // prevent some goofy ahh trailing CR LF

        if (req.getNewPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        }

        try {
            authService.updateUserPassword(token, req.getNewPassword());
            UpdatePassword.Response resp = new UpdatePassword.Response("PASSWORD_UPDATED");
            return ResponseEntity.ok(resp);
        } catch (AuthServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
