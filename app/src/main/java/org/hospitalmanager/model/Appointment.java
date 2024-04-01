package org.hospitalmanager.model;

import java.util.Date;

public class Appointment {

    public static enum Status {
        SUCCESS, WAITING, FAILED
    }

    private String id;
    private String patientId;
    private Date appointmentDate;
    private String doctorId;
    private String content;
    private Status status;
    private Location location;
    private Date dateCreated;

    public Appointment(String id, String patientId, Date appointmentDate, String doctorId, String content, Status status, Location location, Date dateCreated) {
        this.id = id;
        this.patientId = patientId;
        this.appointmentDate = appointmentDate;
        this.doctorId = doctorId;
        this.content = content;
        this.status = status;
        this.location = location;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getContent() {
        return content;
    }

    public Status getStatus() {
        return status;
    }

    public Location getLocation() {
        return location;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
