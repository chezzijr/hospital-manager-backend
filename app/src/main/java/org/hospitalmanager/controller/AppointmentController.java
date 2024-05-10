package org.hospitalmanager.controller;


import org.hospitalmanager.dto.AppointmentWithId;
import org.hospitalmanager.model.Appointment;
import org.hospitalmanager.model.User;
import org.hospitalmanager.service.AppointmentService;
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
@RequestMapping("/appointment")
public class AppointmentController {

    private AppointmentService appointmentService;
    private AuthorizationUtil authUtil;

    @Autowired
    public void setAppointmentController(AppointmentService appointmentService, AuthorizationUtil authorizationUtil) {
        this.appointmentService = appointmentService;
        this.authUtil = authorizationUtil;
    }

    @PostMapping(value = "/create", consumes= MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createAppointment(@RequestHeader HashMap<String, String> headers, @RequestBody Appointment appointment) {
        var token = authUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
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

    @GetMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppointmentById(@RequestHeader HashMap<String, String> headers, @PathVariable String id) throws ExecutionException, InterruptedException {
        var token = authUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid appointment information");
        }
        else {
            AppointmentWithId appointment = appointmentService.getAppointmentById(id);

            if (appointment != null) {
                return ResponseEntity.status(HttpStatus.OK).body(appointment);
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointment is not found");
            }
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteAppointment(@RequestHeader HashMap<String, String> headers, @PathVariable String id, @RequestBody String patientId) throws ExecutionException, InterruptedException {

        var token = authUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
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


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppointment(@RequestHeader HashMap<String, String> headers) throws ExecutionException, InterruptedException {
        var token = authUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        ArrayList<AppointmentWithId> appointments = appointmentService.getAllAppointment();
        if (appointments != null) {
            return ResponseEntity.ok(appointments);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error to get appointment");
        }
    }

    @GetMapping(value = "/patient/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppointmentByPatientId(@RequestHeader HashMap<String, String> headers, @PathVariable String id) throws ExecutionException, InterruptedException {
        var token = authUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        ArrayList<AppointmentWithId> appointments = appointmentService.getAllAppointmentByPatientId(id);
        if (appointments != null) {
            return ResponseEntity.ok(appointments);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error to get appointment");
        }
    }

    @GetMapping(value = "/doctor/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppointmentByDoctorId(@RequestHeader HashMap<String, String> headers, @PathVariable String id) throws ExecutionException, InterruptedException {
        var token = authUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        ArrayList<AppointmentWithId> appointments = appointmentService.getAllAppointmentByDoctorId(id);
        if (appointments != null) {
            return ResponseEntity.ok(appointments);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error to get appointment");
        }
    }

}
