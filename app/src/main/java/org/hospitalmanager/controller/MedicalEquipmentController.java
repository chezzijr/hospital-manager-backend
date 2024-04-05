package org.hospitalmanager.controller;

import java.util.HashMap;
import java.util.Date;

import org.hospitalmanager.model.MedicalEquipment;
import org.hospitalmanager.service.AuthService;
import org.hospitalmanager.service.MedicalEquipmentService;
import org.hospitalmanager.model.User.Role;
import org.hospitalmanager.dto.MedicalEquipment.AddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


/**
 * AdminController class is responsible for handling the requests related to the Medical Equipment.
 * It provides the functionality to add a new Medical Equipment to the system.
 * Must be authorized as admin
 */
@RestController
@RequestMapping("/medicalEquipment")
public class MedicalEquipmentController {
    @Autowired
    private MedicalEquipmentService meService;

    @Autowired
    private AuthService authService;

    private boolean isAuthorized(HashMap<String, String> headers, Role role) {
        String token = headers.get("authorization");
        // Bearer token
        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }

        token = token.substring(7);
        try {
            var firebaseToken = authService.verifyToken(token);
            System.out.println(firebaseToken.getName());
            if (firebaseToken.getName().equals(role.name())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @PostMapping(value="/add", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addMedicalEquipment(@RequestHeader HashMap<String, String> headers, @RequestBody AddRequest req) {
        if (!isAuthorized(headers, Role.ADMIN)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        try {
            var medicalEquipment = new MedicalEquipment(req.getName(), true, req.getAssignedTo(), new Date(), 0);
            String id = meService.addMedicalEquipment(medicalEquipment);
            return ResponseEntity.ok(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicalEquipment> getMedicalEquipment(@PathVariable String id) {
        try {
            MedicalEquipment me = meService.getMedicalEquipment(id);
            return ResponseEntity.ok(me);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(value="/getAll", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicalEquipment[]> getMedicalEquipmentList(String type) {
        try {
            MedicalEquipment[] meList = meService.getMedicalEquipmentList();
            return ResponseEntity.ok(meList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
