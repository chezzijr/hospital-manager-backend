package org.hospitalmanager.dto;

import org.hospitalmanager.model.Medicine;

public class MedicineWithId {

    private final String id;
    private final Medicine medicine;

    public MedicineWithId(String id, Medicine medicine) {
        this.id = id;
        this.medicine = medicine;
    }

    public String getId() {
        return id;
    }

    public Medicine getMedicine() {
        return medicine;
    }
}
