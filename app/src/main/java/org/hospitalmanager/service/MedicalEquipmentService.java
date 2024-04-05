package org.hospitalmanager.service;

import org.hospitalmanager.model.MedicalEquipment;
import org.hospitalmanager.repository.MedicalEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface MedicalEquipmentService {
    public String addMedicalEquipment(MedicalEquipment me) throws Exception;
    public MedicalEquipment getMedicalEquipment(String id) throws Exception;
    public MedicalEquipment[] getMedicalEquipmentList() throws Exception;
}

@Service
class MedicalEquipmentServiceImpl implements MedicalEquipmentService {
    @Autowired
    private MedicalEquipmentRepository repo;

    @Override
    public String addMedicalEquipment(MedicalEquipment me) throws Exception {
        return repo.addMedicalEquipment(me);
    }

    @Override
    public MedicalEquipment getMedicalEquipment(String id) throws Exception {
        return repo.getMedicalEquipment(id);
    }

    @Override
    public MedicalEquipment[] getMedicalEquipmentList() throws Exception {
        return repo.getMedicalEquipmentList();
    }
}
