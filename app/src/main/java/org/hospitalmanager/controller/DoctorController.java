package org.hospitalmanager.controller;


import org.hospitalmanager.model.Doctor;
import org.hospitalmanager.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private DoctorService doctorService;

    @Autowired
    public void DoctorService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> createNewPatient(Doctor doctor) {
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable String id) throws ExecutionException, InterruptedException {
        if (id != null) {
            Doctor doctor = doctorService.getDoctorById(id);

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

    @GetMapping("")
    public ResponseEntity<?> getAllDoctor() throws ExecutionException, InterruptedException {
        ArrayList<Doctor> doctorArrayList = doctorService.getAllDoctor();

        if (doctorArrayList != null) {
            return ResponseEntity.status(HttpStatus.OK).body(doctorArrayList);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error to get doctor list");
        }
    }

}
