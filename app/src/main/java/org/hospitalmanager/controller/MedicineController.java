package org.hospitalmanager.controller;

import org.hospitalmanager.model.Medicine;
import org.hospitalmanager.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/medicine")
public class MedicineController {

    private MedicineService medicineService;

    @Autowired
    public void MedicineService (MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createNewMedicine(Medicine medicine) throws ExecutionException, InterruptedException {
//        // Unauthorized
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User authentication required");
//        }

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


    @GetMapping("/name/{medicineName}")
    public ResponseEntity<?> getMedicineByName(@PathVariable String medicineName) throws ExecutionException, InterruptedException {
//        // Unauthorized
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User authentication required");
//        }

        if (Objects.equals(medicineName, "")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid medicine name");
        }
        else {
            ArrayList<Medicine> medicineArrayList = medicineService.getMedicineByName(medicineName);
            if (!medicineArrayList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(medicineArrayList);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("There is no medicine that matches the name");
        }
    }

    @GetMapping("/type/{medicineType}")
    public ResponseEntity<?> getMedicineByMedicineType(@PathVariable String medicineType) throws ExecutionException, InterruptedException {
//        // Unauthorized
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User authentication required");
//        }

        if (Objects.equals(medicineType, "")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid medicine type");
        }
        else {
            ArrayList<Medicine> medicineArrayList = medicineService.getMedicineByMedicineType(medicineType);
            if (!medicineArrayList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(medicineArrayList);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("There is no medicine that matches the type");
        }
    }

    @GetMapping("/code/{barCode}")
    public ResponseEntity<?> getMedicineByBarCode(@PathVariable String barCode) throws ExecutionException, InterruptedException {
//        // Unauthorized
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User authentication required");
//        }

        if (Objects.equals(barCode, "")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid medicine bar code");
        }
        else {
            Medicine medicine = medicineService.getMedicineByBarCode(barCode);
            if (medicine != null) {
                return ResponseEntity.status(HttpStatus.OK).body(medicine);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("There is no medicine that matches the bar code");
        }
    }
}
