package org.hospitalmanager.controller;

import org.hospitalmanager.dto.MedicineWithId;
import org.hospitalmanager.model.Medicine;
import org.hospitalmanager.model.User;
import org.hospitalmanager.service.MedicineService;
import org.hospitalmanager.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/medicine")
public class MedicineController {

    private MedicineService medicineService;
    private AuthorizationUtil authorizationUtil;

    @Autowired
    public void setMedicineController (MedicineService medicineService, AuthorizationUtil authorizationUtil) {
        this.medicineService = medicineService;
        this.authorizationUtil = authorizationUtil;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createNewMedicine(@RequestHeader HashMap<String, String> headers, @RequestBody Medicine medicine) throws ExecutionException, InterruptedException {
        var token = authorizationUtil.isAuthorized(headers.get("authorization"), User.Role.DOCTOR);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (medicine == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid medicine information");
        }
        else {

            boolean success = medicineService.addNewMedicine(medicine);
            if (success) {
                return ResponseEntity.ok().body("Medicine created successfully");
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to create medicine");
            }
        }
    }


    @GetMapping(value = "/name/{medicineName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMedicineByName(@RequestHeader HashMap<String, String> headers, @PathVariable String medicineName) throws ExecutionException, InterruptedException {
        var token = authorizationUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (Objects.equals(medicineName, "")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid medicine name");
        }
        else {
            ArrayList<MedicineWithId> medicineArrayList = medicineService.getMedicineByName(medicineName);
            if (!medicineArrayList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(medicineArrayList);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("There is no medicine that matches the name");
        }
    }

    @GetMapping(value = "/type/{medicineType}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMedicineByMedicineType(@RequestHeader HashMap<String, String> headers, @PathVariable String medicineType) throws ExecutionException, InterruptedException {
        var token = authorizationUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (Objects.equals(medicineType, "")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid medicine type");
        }
        else {
            ArrayList<MedicineWithId> medicineArrayList = medicineService.getMedicineByMedicineType(medicineType);
            if (!medicineArrayList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(medicineArrayList);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("There is no medicine that matches the type");
        }
    }

    @GetMapping(value = "/code/{barCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMedicineByBarCode(@RequestHeader HashMap<String, String> headers, @PathVariable String barCode) throws ExecutionException, InterruptedException {
        var token = authorizationUtil.isAuthorized(headers.get("authorization"), User.Role.ADMIN, User.Role.DOCTOR, User.Role.NURSE, User.Role.PATIENT);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (Objects.equals(barCode, "")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid medicine bar code");
        }
        else {
            MedicineWithId medicine = medicineService.getMedicineByBarCode(barCode);
            if (medicine != null) {
                return ResponseEntity.status(HttpStatus.OK).body(medicine);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("There is no medicine that matches the bar code");
        }
    }
}
