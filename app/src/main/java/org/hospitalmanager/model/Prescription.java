package org.hospitalmanager.model;

import java.util.ArrayList;
import java.util.Date;

public class Prescription {

    private String id;
    private String patientId;
    private String doctorId;
    private Date dateCreated;
    private Date expiryDate;
    private Integer price;
    private String note;
    private String instructions;
    private String diagnosis;
    private ArrayList<Medicine> medicineArrayList;

    public Prescription(String id, String patientId, String doctorId, Date dateCreated, Date expiryDate, Integer price, String note, String instructions, String diagnosis, ArrayList<Medicine> medicineArrayList) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateCreated = dateCreated;
        this.expiryDate = expiryDate;
        this.price = price;
        this.note = note;
        this.instructions = instructions;
        this.diagnosis = diagnosis;
        this.medicineArrayList = medicineArrayList;
    }

    public Integer getPrice() {
        return price;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public String getId() {
        return id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getNote() {
        return note;
    }

    public ArrayList<Medicine> getMedicineArrayList() {
        return medicineArrayList;
    }
}
