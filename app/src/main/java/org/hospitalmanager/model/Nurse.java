package org.hospitalmanager.model;

import java.util.Date;

public class Nurse extends User {
    private String name;
    private String phoneNumber;
    private Integer yearOfExperience;
    private Integer workingHours;
    private String gender;
    private Date dateOfBirth;
    private String department;

    public Nurse(String id, String email, Role role, String password, String name, String phoneNumber, Integer yearOfExperience, Integer workingHours, String gender, Date dateOfBirth, String department) {
        super(id, email, role, password);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.yearOfExperience = yearOfExperience;
        this.workingHours = workingHours;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.department = department;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getDepartment() {
        return department;
    }

    public Integer getWorkingHours() {
        return workingHours;
    }

    public Integer getYearOfExperience() {
        return yearOfExperience;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
