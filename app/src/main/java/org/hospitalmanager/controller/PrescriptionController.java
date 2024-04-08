package org.hospitalmanager.controller;

import org.hospitalmanager.dto.PrescriptionWithId;
import org.hospitalmanager.model.Medicine;
import org.hospitalmanager.model.Prescription;
import org.hospitalmanager.model.User;
import org.hospitalmanager.service.PrescriptionService;
import org.hospitalmanager.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    private PrescriptionService prescriptionService;
    private AuthorizationUtil authorizationUtil;

    @Autowired
    public void setPrescriptionController(PrescriptionService prescriptionService, AuthorizationUtil authorizationUtil) {
        this.prescriptionService = prescriptionService;
        this.authorizationUtil = authorizationUtil;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createNewPrescription(@RequestHeader HashMap<String, String> headers, @RequestBody Prescription prescription) throws ExecutionException, InterruptedException {

        var token = authorizationUtil.isAuthorized(headers.get("authorization"), User.Role.DOCTOR);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (prescription == null) {
            System.out.println("Prescription can not null.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Prescription can not null");
        }

        boolean success = prescriptionService.createNewPrescription(prescription);

        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Prescription created successfully");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fail to create prescription");
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPrescriptionById(@RequestHeader HashMap<String, String> headers, @PathVariable String id) throws ExecutionException, InterruptedException {
        var token = authorizationUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (Objects.equals(id, "")) {
            System.out.println("Id can not blank");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id can not blank");
        }

        PrescriptionWithId prescription = prescriptionService.getPrescriptionById(id);

        if (prescription == null) {
            System.out.println("Prescription with id " + id + " does not exist");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Prescription is not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(prescription);
    }

    @GetMapping(value = "/patient/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPrescriptionByPatientId(@RequestHeader HashMap<String, String> headers, @PathVariable String id) throws ExecutionException, InterruptedException {
        var token = authorizationUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (Objects.equals(id, "")) {
            System.out.println("Id can not blank");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id can not blank");
        }

        ArrayList<PrescriptionWithId> prescriptionList = prescriptionService.getPrescriptionByPatientId(id);

        if (prescriptionList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Patient don't have prescription");
        }
        return ResponseEntity.status(HttpStatus.OK).body(prescriptionList);

    }

    @GetMapping(value = "/doctor/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPrescriptionByDoctorId(@RequestHeader HashMap<String, String> headers, @PathVariable String id) throws ExecutionException, InterruptedException {
        var token = authorizationUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (Objects.equals(id, "")) {
            System.out.println("Id can not blank");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id can not blank");
        }

        ArrayList<PrescriptionWithId> prescriptionList = prescriptionService.getPrescriptionByDoctorId(id);

        if (prescriptionList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Doctor don't have prescription");
        }
        return ResponseEntity.status(HttpStatus.OK).body(prescriptionList);

    }
}
