package org.hospitalmanager.model;

public class Doctor {

    private String doctorId;
    private String name;
    private String specialization;
    private String qualification;
    private Integer yearOfExperience;
    private String phoneNumber;
    private String email;
    private Integer workingHours;

    public String getDoctorId() {
        return this.doctorId;
    }

    public String getName() {
        return this.name;
    }

    public String getSpecialization() {
        return this.specialization;
    }

    public String getEmail() {
        return this.email;
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

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public void setEmail(String email) {
        this.email = email;
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
}

