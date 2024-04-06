package org.hospitalmanager.service;

import java.util.List;
import org.hospitalmanager.model.MedicalEquipment;
import org.hospitalmanager.repository.MedicalEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.FieldValue;

import java.util.Date;

public interface MedicalEquipmentService {
    public String addMedicalEquipment(MedicalEquipment me) throws Exception;
    public MedicalEquipment getMedicalEquipment(String id) throws Exception;
    public List<MedicalEquipment> getMedicalEquipmentList() throws Exception;
    public MedicalEquipment dispatchMedicalEquipment(String id, String assignedTo) throws Exception;
    public void returnMedicalEquipment(String id, String assignedTo, boolean isAdmin) throws Exception;
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
    public List<MedicalEquipment> getMedicalEquipmentList() throws Exception {
        return repo.getMedicalEquipmentList();
    }

    @Override
    public MedicalEquipment dispatchMedicalEquipment(String id, String assignedTo) throws Exception {
        MedicalEquipment me = repo.getMedicalEquipment(id);
        if (!me.isAvailable()) {
            throw new Exception("Medical equipment is not available");
        }
        repo.updateFieldObject(
            id, 
            "available", false, 
            "latestUsageDate", new Date(), 
            "isBeingUsedBy", assignedTo
        );
        return repo.getMedicalEquipment(id);
    }

    @Override
    public void returnMedicalEquipment(String id, String assignedTo, boolean isAdmin) throws Exception {
        MedicalEquipment me = repo.getMedicalEquipment(id);
        if (me.isAvailable()) {
            throw new Exception("Medical equipment is already available");
        }
        if (!me.getIsBeingUsedBy().equals(assignedTo) && !isAdmin) {
            throw new Exception("Medical equipment is not being used by the specified user");
        }
        repo.updateFieldObject(
            id, 
            "available", true, 
            "latestUsageDate", null, 
            "isBeingUsedBy", null,
            "usageHistory", FieldValue.arrayUnion(new MedicalEquipment.Usage(me.getLatestUsageDate(), new Date(), me.getIsBeingUsedBy()))
        );
    }
}
