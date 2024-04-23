package org.hospitalmanager.controller;


import org.hospitalmanager.dto.NurseWithId;
import org.hospitalmanager.model.Nurse;
import org.hospitalmanager.model.User;
import org.hospitalmanager.service.NurseService;
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
@RequestMapping("/nurse")
public class NurseController {

    private NurseService nurseService;
    private AuthorizationUtil authUtil;

    @Autowired
    public void setNurseController(NurseService nurseService, AuthorizationUtil authUtil) {
        this.nurseService = nurseService;
        this.authUtil = authUtil;
    }

    @GetMapping(value="", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllNurse(@RequestHeader HashMap<String, String> headers) {
        var token = authUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        try {
            ArrayList<NurseWithId> nurseWithIdArrayList = nurseService.getAllNurse();
            return ResponseEntity.status(HttpStatus.OK).body(nurseWithIdArrayList);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getNurseById(@RequestHeader HashMap<String, String> headers, @PathVariable String id) {
        var token = authUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        try {
            NurseWithId nurse = nurseService.getNurseById(id);
            return ResponseEntity.status(HttpStatus.OK).body(nurse);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/department/{department}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getNurseByDepartment(@RequestHeader HashMap<String, String> headers, @PathVariable String department) {
        var token = authUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        try {
            ArrayList<NurseWithId> nurse = nurseService.getNurseByDepartment(department);
            return ResponseEntity.status(HttpStatus.OK).body(nurse);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/setupProfile", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createNewNurse(@RequestHeader HashMap<String, String> headers, @RequestBody Nurse nurse) throws ExecutionException, InterruptedException {
        var token = authUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.NURSE);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorizaed");
        }

        if (nurse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid info nurse");
        }

        if (!token.getName().equals(User.Role.ADMIN.name()) && !nurse.getId().equals(token.getUid())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        boolean success = nurseService.addNewNurse(nurse);

        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Nurse created successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create nurse");
        }
    }
}
