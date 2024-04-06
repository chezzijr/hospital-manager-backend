package org.hospitalmanager.service;

import org.hospitalmanager.model.Patient;
import org.hospitalmanager.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public interface PatientService {
    Patient getPatientById(String patientId) throws ExecutionException, InterruptedException;

    ArrayList<Patient> getAllPatient() throws ExecutionException, InterruptedException;

    boolean createNewPatient(Patient patient);
}

@Service
class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;

    @Autowired
    public void PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient getPatientById(String patientId) throws ExecutionException, InterruptedException {
        return patientRepository.getPatientById(patientId);
    }

    public ArrayList<Patient> getAllPatient() throws ExecutionException, InterruptedException {
        return patientRepository.getAllPatient();
    }

    public boolean createNewPatient(Patient patient) {

        return true;
    }

}