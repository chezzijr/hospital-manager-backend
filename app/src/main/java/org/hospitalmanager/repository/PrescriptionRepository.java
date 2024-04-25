package org.hospitalmanager.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.GenericTypeIndicator;
import org.hospitalmanager.dto.AppointmentWithId;
import org.hospitalmanager.dto.MedicineWithId;
import org.hospitalmanager.dto.PrescriptionWithId;
import org.hospitalmanager.model.Appointment;
import org.hospitalmanager.model.Medicine;
import org.hospitalmanager.model.Prescription;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;

public interface PrescriptionRepository {

    PrescriptionWithId getPrescriptionById(String id) throws ExecutionException, InterruptedException;
    ArrayList<PrescriptionWithId> getPrescriptionByPatientId(String patientId) throws ExecutionException, InterruptedException;
    ArrayList<PrescriptionWithId> getPrescriptionByDoctorId(String doctorId) throws ExecutionException, InterruptedException;
    boolean createNewPrescription(Prescription prescription) throws ExecutionException, InterruptedException;
    ArrayList<PrescriptionWithId> getAllPrescription() throws ExecutionException, InterruptedException;
}

@Repository
class PrescriptionRepositoryImpl implements PrescriptionRepository {

    private final Firestore firestore = FirestoreClient.getFirestore();

    private Prescription convertDocumentSnapshotToPrescriptionClass(DocumentSnapshot documentSnapshot) {
        String id = documentSnapshot.getId();
        String patientId = documentSnapshot.getString("patientId");
        String doctorId = documentSnapshot.getString("doctorId");
        Date dateCreated = documentSnapshot.getDate("dateCreated");
        Date expiryDate = documentSnapshot.getDate("expiryDate");
        Integer price = Objects.requireNonNull(documentSnapshot.getLong("price")).intValue(); // Firestore stores Integer as Long
        String note = documentSnapshot.getString("note");
        String instructions = documentSnapshot.getString("instructions");
        String diagnosis = documentSnapshot.getString("diagnosis");

        ArrayList<Medicine> medicineArrayList = new ArrayList<>();
        Object medicineArrayListObject = documentSnapshot.get("medicineArrayList");

        // Check if the medicineArrayList field is not null and is an instance of ArrayList
        if (medicineArrayListObject instanceof ArrayList<?>) {
            List<Map<String, Object>> medicineList = (ArrayList<Map<String, Object>>) medicineArrayListObject;
            for (Map<String, Object> medicineMap : medicineList) {
                // Convert each map to Medicine object
                Medicine medicine = convertMapToMedicine(medicineMap);
                if (medicine != null) {
                    medicineArrayList.add(medicine);
                }
            }
        }

        // Return a new Prescription object with the retrieved data
        return new Prescription(id, patientId, doctorId, dateCreated, expiryDate, price, note, instructions, diagnosis, medicineArrayList);
    }

    private Medicine convertMapToMedicine(Map<String, Object> medicineMap) {
        // Extract values from the map
        String medicineName = (String) medicineMap.get("medicineName");
        String barCode = (String) medicineMap.get("barCode");
        String description = (String) medicineMap.get("description");
        String manufacturer = (String) medicineMap.get("manufacturer");
        Long priceLong = (Long) medicineMap.get("price"); // Retrieve as Long
        Integer price = priceLong != null ? priceLong.intValue() : null; // Convert to Integer
        com.google.cloud.Timestamp expiryDateTimestamp = (com.google.cloud.Timestamp) medicineMap.get("expiryDate");
        Date expiryDate = expiryDateTimestamp != null ? expiryDateTimestamp.toDate() : null;
        String activeIngredients = (String) medicineMap.get("activeIngredients");
        String dosage = (String) medicineMap.get("dosage");
        String medicineType = (String) medicineMap.get("medicineType");
        Integer inventoryStatus = ((Long) medicineMap.get("inventoryStatus")).intValue(); // Retrieve as Long and cast to Integer

        // Create and return a new Medicine object
        return new Medicine(medicineName, barCode, description, manufacturer, price, expiryDate, activeIngredients, dosage, medicineType, inventoryStatus);
    }


    @Override
    public PrescriptionWithId getPrescriptionById(String prescriptionId) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("id").document(prescriptionId);

        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
        if (documentSnapshot.exists()) {
            String id = documentSnapshot.getId();
            Prescription prescription = convertDocumentSnapshotToPrescriptionClass(documentSnapshot);
            return new PrescriptionWithId(id, prescription);
        }

        return null;
    }
    @Override
    public ArrayList<PrescriptionWithId> getPrescriptionByPatientId(String patientId) throws ExecutionException, InterruptedException {
        ArrayList<PrescriptionWithId> prescriptionList = new ArrayList<>();

        CollectionReference prescriptionCollection = firestore.collection("prescription");

        Query query = prescriptionCollection.whereEqualTo("patientId", patientId);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> queryDocumentSnapshots = querySnapshot.get().getDocuments();

        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
            String id = queryDocumentSnapshot.getId();
            Prescription prescription = convertDocumentSnapshotToPrescriptionClass((DocumentSnapshot) queryDocumentSnapshot);
            prescriptionList.add(new PrescriptionWithId(id, prescription));
        }

        return prescriptionList;
    }

    @Override
    public ArrayList<PrescriptionWithId> getPrescriptionByDoctorId(String doctorId) throws ExecutionException, InterruptedException {
        ArrayList<PrescriptionWithId> prescriptionList = new ArrayList<>();

        CollectionReference prescriptionCollection = firestore.collection("prescription");

        Query query = prescriptionCollection.whereEqualTo("doctorId", doctorId);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> queryDocumentSnapshots = querySnapshot.get().getDocuments();

        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
            String id = queryDocumentSnapshot.getId();
            Prescription prescription = convertDocumentSnapshotToPrescriptionClass((DocumentSnapshot) queryDocumentSnapshot);
            prescriptionList.add(new PrescriptionWithId(id, prescription));
        }

        return prescriptionList;
    }



    @Override
    public boolean createNewPrescription(Prescription prescription) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("prescription").document(prescription.getId());
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();

        if (documentSnapshot.exists()) {
            System.out.println("Prescription with id " + prescription.getId() + "does exist.");
            return false;
        }
        else {

            CollectionReference appointmentsCollection = firestore.collection("prescription");
            appointmentsCollection.document(prescription.getId()).set(prescription);

            System.out.println("Prescription created successfully.");
            return true;
        }
    }

    @Override
    public ArrayList<PrescriptionWithId> getAllPrescription() throws ExecutionException, InterruptedException {
        ArrayList<PrescriptionWithId> prescriptionList = new ArrayList<>();

        ApiFuture<QuerySnapshot> query = firestore.collection("prescription").get();
        QuerySnapshot querySnapshot = query.get();

        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {

            String id = documentSnapshot.getId();
            Prescription prescription = convertDocumentSnapshotToPrescriptionClass(documentSnapshot);
            prescriptionList.add(new PrescriptionWithId(id, prescription));
        }

        return prescriptionList;
    }
}
