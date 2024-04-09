package org.hospitalmanager.dto;

import org.hospitalmanager.model.Doctor;

import javax.print.Doc;

public class DoctorWithId {

    private final String id;
    private final Doctor doctor;

    public DoctorWithId(String id, Doctor doctor) {
        this.id = id;
        this.doctor = doctor;
    }

    public String getId() {
        return id;
    }

    public Doctor getDoctor() {
        return doctor;
    }
}
