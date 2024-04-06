package org.hospitalmanager.service;

import org.hospitalmanager.model.Doctor;
import org.hospitalmanager.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public interface DoctorService {

    Doctor getDoctorById(String doctorId) throws ExecutionException, InterruptedException;

    ArrayList<Doctor> getAllDoctor() throws ExecutionException, InterruptedException;

    boolean createNewDoctor(Doctor doctor);

}

@Service
class DoctorServiceImpl implements DoctorService {

    private DoctorRepository doctorRepository;

    @Autowired
    public void DoctorRepository(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor getDoctorById(String doctorId) throws ExecutionException, InterruptedException {
        return doctorRepository.getDoctorById(doctorId);
    }

    public ArrayList<Doctor> getAllDoctor() throws ExecutionException, InterruptedException {
        return doctorRepository.getAllDoctor();
    }

    public boolean createNewDoctor(Doctor doctor) {

        return true;
    }

}
