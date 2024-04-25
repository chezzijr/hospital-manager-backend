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
    private AuthorizationUtil authorizationUtil;

    @Autowired
    public void setDoctorController(DoctorService doctorService, AuthorizationUtil authorizationUtil) {
        this.doctorService = doctorService;
        this.authorizationUtil = authorizationUtil;
    }

    @PostMapping(value = "/setupProfile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createNewPatient(@RequestBody Doctor doctor) {
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
    public ResponseEntity<?> getDoctorById(@PathVariable String id) throws ExecutionException, InterruptedException {

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
    public ResponseEntity<?> getAllDoctor() throws ExecutionException, InterruptedException {

        ArrayList<DoctorWithId> doctorArrayList = doctorService.getAllDoctor();

        if (doctorArrayList != null) {
            return ResponseEntity.status(HttpStatus.OK).body(doctorArrayList);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error to get doctor list");
        }
    }

    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorByName(@PathVariable String name) throws ExecutionException, InterruptedException {

        ArrayList<DoctorWithId> doctorWithIdArrayList = doctorService.getDoctorByName(name);

        if (doctorWithIdArrayList != null) {
            return ResponseEntity.status(HttpStatus.OK).body(doctorWithIdArrayList);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error to get doctor list");
        }
    }

    @GetMapping(value = "/specialization/{specialization}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorBySpecialization(@PathVariable String specialization) throws ExecutionException, InterruptedException {

        ArrayList<DoctorWithId> doctorWithIdArrayList = doctorService.getDoctorBySpecialization(specialization);

        if (doctorWithIdArrayList != null) {
            return ResponseEntity.status(HttpStatus.OK).body(doctorWithIdArrayList);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error to get doctor list");
        }
    }

}
