package org.hospitalmanager.dto;

import org.hospitalmanager.model.Patient;

public class PaitentWithId {

    private final String id;
    private final Patient patient;

    public PaitentWithId(String id, Patient patient) {
        this.id = id;
        this.patient = patient;
    }

    public String getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }
}
