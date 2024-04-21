package org.hospitalmanager.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.hospitalmanager.dto.MedicineWithId;
import org.hospitalmanager.model.Medicine;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public interface MedicineRepository {

    ArrayList<MedicineWithId> getAllMedicine() throws ExecutionException, InterruptedException;

    ArrayList<MedicineWithId> getMedicineByName(String medicineName) throws ExecutionException, InterruptedException;

    ArrayList<MedicineWithId> getMedicineByMedicineType(String medicineType) throws ExecutionException, InterruptedException;

    MedicineWithId getMedicineByBarCode(String barCode) throws ExecutionException, InterruptedException;

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

    @Override
    public ArrayList<MedicineWithId> getAllMedicine() throws ExecutionException, InterruptedException {
        ArrayList<MedicineWithId> medicineList = new ArrayList<>();

        ApiFuture<QuerySnapshot> query = firestore.collection("medicine").get();
        QuerySnapshot querySnapshot = query.get();

        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
            String id = documentSnapshot.getId();
            Medicine medicine = convertDocumentSnapshotToMedicineClass(documentSnapshot);
            medicineList.add(new MedicineWithId(id, medicine));
        }

        return medicineList;
    }

    @Override
    public ArrayList<MedicineWithId> getMedicineByName(String medicineName) throws ExecutionException, InterruptedException {
        ArrayList<MedicineWithId> medicineList = new ArrayList<>();

        CollectionReference medicineCollection = firestore.collection("medicine");

        Query query = medicineCollection.whereEqualTo("medicineName", medicineName);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> queryDocumentSnapshots = querySnapshot.get().getDocuments();

        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
            String id = queryDocumentSnapshot.getId();
            Medicine medicine = convertDocumentSnapshotToMedicineClass((DocumentSnapshot) queryDocumentSnapshot);
            medicineList.add(new MedicineWithId(id, medicine));
        }

        return medicineList;
    }

    @Override
    public ArrayList<MedicineWithId> getMedicineByMedicineType(String medicineType) throws ExecutionException, InterruptedException {
        ArrayList<MedicineWithId> medicineList = new ArrayList<>();

        CollectionReference medicineCollection = firestore.collection("medicine");

        Query query = medicineCollection.whereEqualTo("medicineType", medicineType);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> queryDocumentSnapshots = querySnapshot.get().getDocuments();

        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
            String id = queryDocumentSnapshot.getId();
            Medicine medicine = convertDocumentSnapshotToMedicineClass((DocumentSnapshot) queryDocumentSnapshot);
            medicineList.add(new MedicineWithId(id, medicine));
        }

        return medicineList;
    }
    @Override
    public MedicineWithId getMedicineByBarCode(String barCode) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("medicine").document(barCode);

        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
        if (documentSnapshot.exists()) {
            String id = documentSnapshot.getId();
            return new MedicineWithId(id, convertDocumentSnapshotToMedicineClass(documentSnapshot));
        }

        return null;
    }

    @Override
    public boolean addNewMedicine(Medicine medicine) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("medicine").document(medicine.getBarCode());
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();

        if (documentSnapshot.exists()) {
            System.out.println("Medicine with bar code " + medicine.getBarCode() + "does exist.");
            return false;
        }
        else {

            CollectionReference appointmentsCollection = firestore.collection("medicine");
            appointmentsCollection.document(medicine.getBarCode()).set(medicine);

            System.out.println("Medicine with barCode created successfully.");
            return true;
        }
    }

    @Override
    public boolean editInfoMedicineByBarCode(Medicine medicine) {

        //
        return false;
    }

}
