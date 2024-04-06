package org.hospitalmanager.controller;

import java.util.HashMap;
import java.util.List;

import org.hospitalmanager.model.MedicalEquipment;
import org.hospitalmanager.service.MedicalEquipmentService;
import org.hospitalmanager.model.User.Role;
import org.hospitalmanager.dto.MedicalEquipment.*;
import org.hospitalmanager.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


/**
 * AdminController class is responsible for handling the requests related to the Medical Equipment.
 * It provides the functionality to add a new Medical Equipment to the system.
 * Must be authorized
 */
@RestController
@RequestMapping("/medicalEquipment")
public class MedicalEquipmentController {
    @Autowired
    private MedicalEquipmentService service;

    @Autowired
    private AuthorizationUtil authUtil;

    @PostMapping(value="/add", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addMedicalEquipment(@RequestHeader HashMap<String, String> headers, @RequestBody AddRequest req) {
        if (authUtil.isAuthorized(headers.get("authorization"), Role.ADMIN) == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        try {
            var medicalEquipment = new MedicalEquipment(req.getName(), req.getLatestMaintenanceDate());
            String id = service.addMedicalEquipment(medicalEquipment);
            return ResponseEntity.ok(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicalEquipment> getMedicalEquipment(@PathVariable String id) {
        try {
            MedicalEquipment me = service.getMedicalEquipment(id);
            return ResponseEntity.ok(me);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(value="/getAll", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicalEquipment>> getMedicalEquipmentList(String type) {
        try {
            var meList = service.getMedicalEquipmentList();
            return ResponseEntity.ok(meList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Routes for dispatching the medical equipment to the faculty
    // And for returning the medical equipment to the hospital
    @PostMapping(value="/dispatch", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> dispatchMedicalEquipment(@RequestHeader HashMap<String, String> headers, @RequestBody DispatchRequest req) {
        var token = authUtil.isAuthorized(headers.get("authorization"), Role.ADMIN, Role.DOCTOR, Role.NURSE);
        if (token == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        System.out.println(req.getUid());

        try {
            if (req.getUid() != null && token.getName().equals("ADMIN")) {
                service.dispatchMedicalEquipment(req.getId(), req.getUid());
            } else {
                service.dispatchMedicalEquipment(req.getId(), token.getUid());
            }
            return ResponseEntity.ok("Medical Equipment dispatched successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value="/return", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> returnMedicalEquipment(@RequestHeader HashMap<String, String> headers, @RequestBody ReturnRequest req) {
        var token = authUtil.isAuthorized(headers.get("authorization"), Role.ADMIN, Role.DOCTOR, Role.NURSE);
        if (token == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        try {
            service.returnMedicalEquipment(req.getId(), token.getUid(), token.getName().equals("ADMIN"));
            return ResponseEntity.ok("Medical Equipment returned successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
