package org.hospitalmanager.controller;

import org.hospitalmanager.model.Patient;
import org.hospitalmanager.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private PatientService patientService;

    @Autowired
    public void PatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> createNewPatient(Patient patient) {
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable String id) throws ExecutionException, InterruptedException {
        if (id != null) {
            Patient patient = patientService.getPatientById(id);

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

    @GetMapping("")
    public ResponseEntity<?> getAllPatient() throws ExecutionException, InterruptedException {
        ArrayList<Patient> patientArrayList = patientService.getAllPatient();

        if (patientArrayList != null) {
            return ResponseEntity.status(HttpStatus.OK).body(patientArrayList);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error to get patient list");
        }
    }
}
