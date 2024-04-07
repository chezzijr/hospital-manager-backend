package org.hospitalmanager.controller;

import org.hospitalmanager.dto.PrescriptionWithId;
import org.hospitalmanager.model.Prescription;
import org.hospitalmanager.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    private PrescriptionService prescriptionService;

    @Autowired
    public void setPrescriptionService(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createNewPrescription(@RequestBody Prescription prescription) throws ExecutionException, InterruptedException {
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getPrescriptionById(@PathVariable String id) throws ExecutionException, InterruptedException {
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

    @GetMapping("/patient/{id}")
    public ResponseEntity<?> getPrescriptionByPatientId(@PathVariable String id) throws ExecutionException, InterruptedException {
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

    @GetMapping("/doctor/{id}")
    public ResponseEntity<?> getPrescriptionByDoctorId(@PathVariable String id) throws ExecutionException, InterruptedException {
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
