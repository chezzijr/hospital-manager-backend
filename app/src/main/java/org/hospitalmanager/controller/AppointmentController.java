package org.hospitalmanager.controller;


import org.hospitalmanager.model.Appointment;
import org.hospitalmanager.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private AppointmentService appointmentService;

    @Autowired
    public void AppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping(value = "/create")
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

            boolean success = appointmentService.createAppointment(appointment);
            if (success) {
                return ResponseEntity.ok().body("Appointment created successfully");
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to create appointment");
            }
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable String id) throws ExecutionException, InterruptedException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User authentication required");
        }

        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid appointment information");
        }
        else {
            Appointment appointment = appointmentService.getAppointmentById(id);

            if (appointment != null) {
                return ResponseEntity.status(HttpStatus.OK).body(appointment);
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointment is not found");
            }
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable String id, @RequestBody String patientId) throws ExecutionException, InterruptedException {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User authentication required");
        }

        // Appointment is null
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid appointment information");
        }
        else {
            boolean isAppointmentBelongToPatient = appointmentService.isAppointmentBelongToPatient(patientId, id);
            if (!isAppointmentBelongToPatient) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Appointment not belong");
            }
            boolean success = appointmentService.deleteAppointmentById(id);

            if (success) {
                return ResponseEntity.ok().body("Appointment is deleted");
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete appointment");
            }
        }
    }


    @GetMapping(value = "/")
    public ResponseEntity<?> getAppointment() throws ExecutionException, InterruptedException {
        List<Appointment> appointments = appointmentService.getAllAppointment();
        if (appointments != null) {
            return ResponseEntity.ok(appointments);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error to get appointment");
        }
    }

}
