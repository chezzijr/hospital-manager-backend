package org.hospitalmanager.controller;


import org.hospitalmanager.service.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nurse")
public class NurseController {

    private NurseService nurseService;

    @Autowired
    public void setNurseService(NurseService nurseService) {
        this.nurseService = nurseService;
    }

//    @GetMapping(value="", produces= MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> getAllNurse() {
//
//    }
}
