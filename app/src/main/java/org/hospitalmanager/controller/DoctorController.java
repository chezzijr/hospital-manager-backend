package org.hospitalmanager.controller;


import org.hospitalmanager.dto.DoctorWithId;
import org.hospitalmanager.model.Doctor;
import org.hospitalmanager.model.User;
import org.hospitalmanager.service.DoctorService;
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
@RequestMapping("/doctor")
public class DoctorController {

    private DoctorService doctorService;
    private AuthorizationUtil authUtil;

    @Autowired
    public void setDoctorController(DoctorService doctorService, AuthorizationUtil authorizationUtil) {
        this.doctorService = doctorService;
        this.authUtil = authorizationUtil;
    }

    @PostMapping(value = "/setupProfile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createNewDoctor(@RequestHeader HashMap<String, String> headers, @RequestBody Doctor doctor) {
        var token = authUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (doctor == null) {
            return ResponseEntity.badRequest().body("Invalid doctor information");
        }
        else {

            boolean success = doctorService.createNewDoctor(doctor);
            if (success) {
                return ResponseEntity.ok().body("Doctor created successfully");
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to create doctor");
            }
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorById(@RequestHeader HashMap<String, String> headers, @PathVariable String id) throws ExecutionException, InterruptedException {

        var token = authUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (id != null) {
            DoctorWithId doctor = doctorService.getDoctorById(id);

            if (doctor != null) {
                return ResponseEntity.status(HttpStatus.OK).body(doctor);
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot find doctor");
            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id cannot null");
        }
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllDoctor(@RequestHeader HashMap<String, String> headers) throws ExecutionException, InterruptedException {
        var token = authUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        ArrayList<DoctorWithId> doctorArrayList = doctorService.getAllDoctor();

        if (doctorArrayList != null) {
            return ResponseEntity.status(HttpStatus.OK).body(doctorArrayList);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error to get doctor list");
        }
    }

    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorByName(@RequestHeader HashMap<String, String> headers, @PathVariable String name) throws ExecutionException, InterruptedException {
        var token = authUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        ArrayList<DoctorWithId> doctorWithIdArrayList = doctorService.getDoctorByName(name);

        if (doctorWithIdArrayList != null) {
            return ResponseEntity.status(HttpStatus.OK).body(doctorWithIdArrayList);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error to get doctor list");
        }
    }

    @GetMapping(value = "/specialization/{specialization}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorBySpecialization(@RequestHeader HashMap<String, String> headers, @PathVariable String specialization) throws ExecutionException, InterruptedException {
        var token = authUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        ArrayList<DoctorWithId> doctorWithIdArrayList = doctorService.getDoctorBySpecialization(specialization);

        if (doctorWithIdArrayList != null) {
            return ResponseEntity.status(HttpStatus.OK).body(doctorWithIdArrayList);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error to get doctor list");
        }
    }

}
