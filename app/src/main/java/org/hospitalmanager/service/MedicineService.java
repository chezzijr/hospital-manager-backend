package org.hospitalmanager.service;

import org.hospitalmanager.dto.MedicineWithId;
import org.hospitalmanager.model.Medicine;
import org.hospitalmanager.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public interface MedicineService {

    ArrayList<MedicineWithId> getMedicines() throws ExecutionException, InterruptedException;

    ArrayList<MedicineWithId> getMedicineByName(String medicineName) throws ExecutionException, InterruptedException;

    ArrayList<MedicineWithId> getMedicineByMedicineType(String medicineType) throws ExecutionException, InterruptedException;

    MedicineWithId getMedicineByBarCode(String barCode) throws ExecutionException, InterruptedException;

    boolean addNewMedicine(Medicine medicine) throws ExecutionException, InterruptedException;

    boolean editInfoMedicineByBarCode(Medicine medicine);

}

@Service
class MedicineServiceImpl implements MedicineService {

    private MedicineRepository medicineRepository;

    @Autowired
    public void MedicineRepository(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public ArrayList<MedicineWithId> getMedicines() throws ExecutionException, InterruptedException {
        return medicineRepository.getAllMedicine();
    }

    @Override
    public ArrayList<MedicineWithId> getMedicineByName(String medicineName) throws ExecutionException, InterruptedException {
        if (Objects.equals(medicineName, "")) {
            System.out.println("Medicine name cannot be blank");
            return null;
        }

        return medicineRepository.getMedicineByName(medicineName);
    }

    @Override
    public ArrayList<MedicineWithId> getMedicineByMedicineType(String medicineType) throws ExecutionException, InterruptedException {
        if (Objects.equals(medicineType, "")) {
            System.out.println("Medicine type cannot be blank");
            return null;
        }

        return medicineRepository.getMedicineByMedicineType(medicineType);
    }

    @Override
    public MedicineWithId getMedicineByBarCode(String barCode) throws ExecutionException, InterruptedException {
        if (Objects.equals(barCode, "")) {
            System.out.println("Medicine barCode cannot be blank");
            return null;
        }

        return medicineRepository.getMedicineByBarCode(barCode);
    }

    public boolean addNewMedicine(Medicine medicine) throws ExecutionException, InterruptedException {
        return  medicineRepository.addNewMedicine(medicine);
    }

    @Override
    public boolean editInfoMedicineByBarCode(Medicine medicine) {


        //
        return medicineRepository.editInfoMedicineByBarCode(medicine);
    }

}
