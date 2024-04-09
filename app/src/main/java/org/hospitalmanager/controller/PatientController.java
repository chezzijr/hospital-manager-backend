package org.hospitalmanager.controller;

import org.hospitalmanager.dto.PaitentWithId;
import org.hospitalmanager.model.Medicine;
import org.hospitalmanager.model.Patient;
import org.hospitalmanager.model.User;
import org.hospitalmanager.service.PatientService;
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
@RequestMapping("/patient")
public class PatientController {

    private PatientService patientService;
    private AuthorizationUtil authorizationUtil;

    @Autowired
    public void setPatientController(PatientService patientService, AuthorizationUtil authorizationUtil) {
        this.patientService = patientService;
        this.authorizationUtil = authorizationUtil;
    }

    @PostMapping(value = "/setupProfile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createNewPatient(@RequestBody Patient patient) throws ExecutionException, InterruptedException {
        if (patient == null) {
            return ResponseEntity.badRequest().body("Invalid patient information");
        }
        else {

            boolean success = patientService.createNewPatient(patient);
            if (success) {
                return ResponseEntity.ok().body("Patient created successfully");
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to create patient");
            }
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPatientById(@RequestHeader HashMap<String, String> headers, @PathVariable String id) throws ExecutionException, InterruptedException {
        var token = authorizationUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (id != null) {
            PaitentWithId patient = patientService.getPatientById(id);

            if (patient != null) {
                return ResponseEntity.status(HttpStatus.OK).body(patient);
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot find patient");
            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id cannot null");
        }
    }

    @GetMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllPatient(@RequestHeader HashMap<String, String> headers) throws ExecutionException, InterruptedException {
        var token = authorizationUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        ArrayList<PaitentWithId> patientArrayList = patientService.getAllPatient();

        if (patientArrayList != null) {
            return ResponseEntity.status(HttpStatus.OK).body(patientArrayList);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error to get patient list");
        }
    }
}
