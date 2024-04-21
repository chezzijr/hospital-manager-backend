package org.hospitalmanager.controller;

import org.hospitalmanager.dto.FeedbackWithId;
import org.hospitalmanager.model.User;
import org.hospitalmanager.service.FeedbackService;
import org.hospitalmanager.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private FeedbackService feedbackService;
    private AuthorizationUtil authorizationUtil;

    @Autowired
    public void setFeedbackController(FeedbackService feedbackService, AuthorizationUtil authorizationUtil) {
        this.feedbackService = feedbackService;
        this.authorizationUtil = authorizationUtil;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllFeedback(@RequestHeader HashMap<String, String> headers) throws ExecutionException, InterruptedException {
        var token = authorizationUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        ArrayList<FeedbackWithId> feedbackList = feedbackService.getAllFeedback();
        return ResponseEntity.status(HttpStatus.OK).body(feedbackList);

    }

    @GetMapping(value = "/patient/{patientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFeedBackByPatientId(@RequestHeader HashMap<String, String> headers, @PathVariable String patientId) throws ExecutionException, InterruptedException {
        var token = authorizationUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        ArrayList<FeedbackWithId> feedbackList = feedbackService.getFeedbackByPatientId(patientId);
        return ResponseEntity.status(HttpStatus.OK).body(feedbackList);
    }



}
