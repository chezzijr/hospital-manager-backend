package org.hospitalmanager.controller;

import org.hospitalmanager.model.Appointment;
import org.hospitalmanager.model.Patient;
import org.hospitalmanager.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private PatientService patientService;

    @Autowired
    public void PatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping(value = "/create-appointment")
    public ResponseEntity<String> createAppointment(@RequestBody Appointment appointment) {

        // Unauthorized
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User authentication required");
        }

        // Appointment is null
        if (appointment == null) {
            return ResponseEntity.badRequest().body("Invalid appointment information");
        }
        else {

            boolean success = patientService.createAppointment(appointment);
            if (success) {
                String patientId = appointment.getPatientId();
                String doctorId = appointment.getDoctorId();
                if (patientId == null) {
                    return ResponseEntity.badRequest().body("Invalid patient ID");
                }

                if (doctorId == null) {
                    return ResponseEntity.badRequest().body("Invalid doctor ID");
                }

                Patient patient = patientService.getPatientById(patientId);
                patient.addNewAppointment(appointment);

                patientService.updateByAddPatient(patient);
                return ResponseEntity.ok().body("Appointment created successfully");
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to create appointment");
            }
        }
    }

    @DeleteMapping(value = "/delete-appointment/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable String id, @RequestBody String patientId) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User authentication required");
        }

        // Appointment is null
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid appointment information");
        }
        else {
            boolean isAppointmentBelongToPatient = patientService.isAppointmentBelongToPatient(patientId, id);
            if (!isAppointmentBelongToPatient) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Appointment not belong");
            }
            boolean success = patientService.deleteAppointmentById(id);

            if (success) {
                return ResponseEntity.ok().body("Appointment is deleted");
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete appointment");
            }
        }
    }


    @GetMapping(value = "/")
    public ResponseEntity<List<Appointment>> getAppointment()
}
