package org.hospitalmanager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hospitalmanager.model.User.Role.PATIENT;

public class Patient extends User {
    private String gender;
    private Date dateOfBirth;


    public Patient(String id, String email, Role role, String password, String gender, Date dateOfBirth) {
        super(id, email, role, password);
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }


    public String getGender() {
        return gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


}
