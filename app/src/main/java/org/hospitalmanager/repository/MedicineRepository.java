package org.hospitalmanager.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.hospitalmanager.model.Medicine;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public interface MedicineRepository {

    ArrayList<Medicine> getMedicineByName(String medicineName) throws ExecutionException, InterruptedException;

    ArrayList<Medicine> getMedicineByMedicineType(String medicineType) throws ExecutionException, InterruptedException;

    Medicine getMedicineByBarCode(String barCode) throws ExecutionException, InterruptedException;

    boolean addNewMedicine(Medicine medicine) throws ExecutionException, InterruptedException;

    boolean editInfoMedicineByBarCode(Medicine medicine);

}

@Repository
class MedicineRepositoryImpl implements MedicineRepository {

    private final Firestore firestore = FirestoreClient.getFirestore();

    private Medicine convertDocumentSnapshotToMedicineClass(DocumentSnapshot documentSnapshot) {

        String medicineName = documentSnapshot.getString("medicineName");
        String barCode = documentSnapshot.getString("barCode");
        String description = documentSnapshot.getString("description");
        String manufacturer = documentSnapshot.getString("manufacturer");
        Integer price = Objects.requireNonNull(documentSnapshot.getDouble("price")).intValue();
        Date expiryDate = documentSnapshot.getDate("expiryDate");
        String activeIngredients = documentSnapshot.getString("activeIngredients");
        String dosage = documentSnapshot.getString("activeIngredients");
        String medicineType = documentSnapshot.getString("activeIngredients");
        Integer inventoryStatus = Objects.requireNonNull(documentSnapshot.getDouble("inventoryStatus")).intValue();

        return new Medicine(medicineName, barCode, description, manufacturer, price, expiryDate, activeIngredients, dosage, medicineType, inventoryStatus);
    }
    public ArrayList<Medicine> getMedicineByName(String medicineName) throws ExecutionException, InterruptedException {
        ArrayList<Medicine> medicineList = new ArrayList<>();

        CollectionReference appointmentsCollection = firestore.collection("medicine");

        Query query = appointmentsCollection.whereEqualTo("medicineName", medicineName);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> queryDocumentSnapshots = querySnapshot.get().getDocuments();

        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
            Medicine medicine = convertDocumentSnapshotToMedicineClass((DocumentSnapshot) queryDocumentSnapshot);
            medicineList.add(medicine);
        }

        return medicineList;
    }

    public ArrayList<Medicine> getMedicineByMedicineType(String medicineType) throws ExecutionException, InterruptedException {
        ArrayList<Medicine> medicineList = new ArrayList<>();

        CollectionReference appointmentsCollection = firestore.collection("medicine");

        Query query = appointmentsCollection.whereEqualTo("medicineType", medicineType);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> queryDocumentSnapshots = querySnapshot.get().getDocuments();

        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
            Medicine medicine = convertDocumentSnapshotToMedicineClass((DocumentSnapshot) queryDocumentSnapshot);
            medicineList.add(medicine);
        }

        return medicineList;
    }

    public Medicine getMedicineByBarCode(String barCode) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("medicine").document(barCode);

        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
        if (documentSnapshot.exists()) {

            return convertDocumentSnapshotToMedicineClass(documentSnapshot);
        }

        return null;
    }

    public boolean addNewMedicine(Medicine medicine) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("medicine").document(medicine.getBarCode());
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();

        if (documentSnapshot.exists()) {
            System.out.println("Medicine created successfully.");
            return true;
        }
        else {
            System.out.println("Doctor with barCode " + medicine.getBarCode() + " does not exist.");
            return false;
        }
    }

    public boolean editInfoMedicineByBarCode(Medicine medicine) {

        //
        return false;
    }

}
