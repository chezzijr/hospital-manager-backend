package org.hospitalmanager.service;

import org.hospitalmanager.dto.NurseWithId;
import org.hospitalmanager.model.Nurse;
import org.hospitalmanager.repository.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public interface NurseService {

    ArrayList<NurseWithId> getAllNurse() throws ExecutionException, InterruptedException;

    NurseWithId getNurseById(String id) throws ExecutionException, InterruptedException;

    ArrayList<NurseWithId> getNurseByDepartment(String department) throws ExecutionException, InterruptedException;

    boolean addNewNurse(Nurse nurse) throws ExecutionException, InterruptedException;

}

@Service
class NurseServiceImpl implements NurseService {

    private NurseRepository nurseRepository;

    @Autowired
    public void setNurseRepository(NurseRepository nurseRepository) {
        this.nurseRepository = nurseRepository;
    }

    @Override
    public ArrayList<NurseWithId> getAllNurse() throws ExecutionException, InterruptedException {
        return nurseRepository.getAllNurse();
    }

    @Override
    public NurseWithId getNurseById(String id) throws ExecutionException, InterruptedException {
        return nurseRepository.getNurseById(id);
    }

    @Override
    public ArrayList<NurseWithId> getNurseByDepartment(String department) throws ExecutionException, InterruptedException {
        return nurseRepository.getNurseByDepartment(department);
    }

    @Override
    public boolean addNewNurse(Nurse nurse) throws ExecutionException, InterruptedException {
        return nurseRepository.addNewNurse(nurse);
    }
}
