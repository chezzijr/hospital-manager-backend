package org.hospitalmanager.model;

import java.util.Date;

public class Doctor extends User {

    private String name;
    private String specialization;
    private String qualification;
    private Integer yearOfExperience;
    private String phoneNumber;
    private Integer workingHours;
    private String gender;
    private Date dateOfBirth;

    public Doctor(String id, String email, Role role, String password, String name, String specialization, String qualification, Integer yearOfExperience, String phoneNumber, Integer workingHours, String gender, Date dateOfBirth) {
        super(id, email, role, password);
        this.name = name;
        this.specialization = specialization;
        this.qualification = qualification;
        this.yearOfExperience = yearOfExperience;
        this.phoneNumber = phoneNumber;
        this.workingHours = workingHours;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return this.name;
    }

    public String getSpecialization() {
        return this.specialization;
    }

    public String getQualification() {
        return this.qualification;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Integer getYearOfExperience() {
        return this.yearOfExperience;
    }

    public Integer getWorkingHours() {
        return this.workingHours;
    }

    public String getGender() {
        return this.gender;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setWorkingHours(Integer workingHours) {
        this.workingHours = workingHours;
    }

    public void setYearOfExperience(Integer yearOfExperience) {
        this.yearOfExperience = yearOfExperience;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}

