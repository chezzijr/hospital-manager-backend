package org.hospitalmanager.service;

import org.hospitalmanager.dto.DoctorWithId;
import org.hospitalmanager.model.Doctor;
import org.hospitalmanager.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public interface DoctorService {

    DoctorWithId getDoctorById(String doctorId) throws ExecutionException, InterruptedException;

    ArrayList<DoctorWithId> getAllDoctor() throws ExecutionException, InterruptedException;

    boolean createNewDoctor(Doctor doctor);

    ArrayList<DoctorWithId> getDoctorByName(String name) throws ExecutionException, InterruptedException;

    ArrayList<DoctorWithId> getDoctorBySpecialization(String specialization) throws ExecutionException, InterruptedException;

}

@Service
class DoctorServiceImpl implements DoctorService {

    private DoctorRepository doctorRepository;

    @Autowired
    public void DoctorRepository(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public DoctorWithId getDoctorById(String doctorId) throws ExecutionException, InterruptedException {
        return doctorRepository.getDoctorById(doctorId);
    }

    @Override
    public ArrayList<DoctorWithId> getAllDoctor() throws ExecutionException, InterruptedException {
        return doctorRepository.getAllDoctor();
    }

    @Override
    public boolean createNewDoctor(Doctor doctor) {
        return doctorRepository.createNewDoctor(doctor);
    }

    @Override
    public ArrayList<DoctorWithId> getDoctorByName(String name) throws ExecutionException, InterruptedException {
        return doctorRepository.getDoctorByName(name);
    }

    @Override
    public ArrayList<DoctorWithId> getDoctorBySpecialization(String specialization) throws ExecutionException, InterruptedException {
        return doctorRepository.getDoctorBySpecialization(specialization);
    }

}
