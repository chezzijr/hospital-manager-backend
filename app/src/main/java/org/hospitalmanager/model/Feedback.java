package org.hospitalmanager.model;

import java.util.Date;

public class Feedback {
    private String id;
    private String patientId;
    private String doctorId;
    private String content;
    private Integer ratingStar;

    private Date dateCreated;

    public Feedback(String id, String patientId, String doctorId, String content, Integer ratingStar, Date dateCreated) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.content = content;
        this.ratingStar = ratingStar;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getContent() {
        return content;
    }

    public Integer getRatingStar() {
        return ratingStar;
    }

    public Date getDateCreated() {
        return dateCreated;
    }
}
