package org.hospitalmanager.service;

import org.hospitalmanager.dto.PrescriptionWithId;
import org.hospitalmanager.model.Prescription;
import org.hospitalmanager.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public interface PrescriptionService {

    PrescriptionWithId getPrescriptionById(String id) throws ExecutionException, InterruptedException;
    ArrayList<PrescriptionWithId> getPrescriptionByPatientId(String patientId) throws ExecutionException, InterruptedException;
    ArrayList<PrescriptionWithId> getPrescriptionByDoctorId(String doctorId) throws ExecutionException, InterruptedException;
    boolean createNewPrescription(Prescription prescription) throws ExecutionException, InterruptedException;
}

@Service
class PrescriptionServiceImpl implements PrescriptionService {

    private PrescriptionRepository prescriptionRepository;

    @Autowired
    public void setPrescriptionRepository(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public PrescriptionWithId getPrescriptionById(String id) throws ExecutionException, InterruptedException {
        return prescriptionRepository.getPrescriptionById(id);
    }

    @Override
    public ArrayList<PrescriptionWithId> getPrescriptionByPatientId(String patientId) throws ExecutionException, InterruptedException {
        return prescriptionRepository.getPrescriptionByPatientId(patientId);
    }

    @Override
    public ArrayList<PrescriptionWithId> getPrescriptionByDoctorId(String doctorId) throws ExecutionException, InterruptedException {
        return prescriptionRepository.getPrescriptionByDoctorId(doctorId);
    }

    @Override
    public boolean createNewPrescription(Prescription prescription) throws ExecutionException, InterruptedException {
        return prescriptionRepository.createNewPrescription(prescription);
    }
}
