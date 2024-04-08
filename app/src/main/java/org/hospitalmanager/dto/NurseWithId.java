package org.hospitalmanager.dto;

import org.hospitalmanager.model.Nurse;

public class NurseWithId {
    private String id;
    private Nurse nurse;

    public NurseWithId(String id, Nurse nurse) {
        this.id = id;
        this.nurse = nurse;
    }

    public String getId() {
        return id;
    }

    public Nurse getNurse() {
        return nurse;
    }
}
