package org.hospitalmanager.dto;

import org.hospitalmanager.model.Prescription;

public class PrescriptionWithId {

    private final String id;
    private final Prescription prescription;

    public PrescriptionWithId(String id, Prescription prescription) {
        this.id = id;
        this.prescription = prescription;
    }

    public String getId() {
        return id;
    }

    public Prescription getPrescription() {
        return prescription;
    }
}
