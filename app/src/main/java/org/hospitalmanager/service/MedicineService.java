package org.hospitalmanager.service;

import org.hospitalmanager.model.Medicine;
import org.hospitalmanager.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public interface MedicineService {

    ArrayList<Medicine> getMedicineByName(String medicineName) throws ExecutionException, InterruptedException;

    ArrayList<Medicine> getMedicineByMedicineType(String medicineType) throws ExecutionException, InterruptedException;

    Medicine getMedicineByBarCode(String barCode) throws ExecutionException, InterruptedException;

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

    public ArrayList<Medicine> getMedicineByName(String medicineName) throws ExecutionException, InterruptedException {
        if (Objects.equals(medicineName, "")) {
            System.out.println("Medicine name cannot be blank");
            return null;
        }

        return medicineRepository.getMedicineByName(medicineName);
    }

    public ArrayList<Medicine> getMedicineByMedicineType(String medicineType) throws ExecutionException, InterruptedException {
        if (Objects.equals(medicineType, "")) {
            System.out.println("Medicine type cannot be blank");
            return null;
        }

        return medicineRepository.getMedicineByMedicineType(medicineType);
    }

    public Medicine getMedicineByBarCode(String barCode) throws ExecutionException, InterruptedException {
        if (Objects.equals(barCode, "")) {
            System.out.println("Medicine barCode cannot be blank");
            return null;
        }

        return medicineRepository.getMedicineByBarCode(barCode);
    }

    public boolean addNewMedicine(Medicine medicine) throws ExecutionException, InterruptedException {
        return  medicineRepository.addNewMedicine(medicine);
    }

    public boolean editInfoMedicineByBarCode(Medicine medicine) {


        //
        return medicineRepository.editInfoMedicineByBarCode(medicine);
    }

}
