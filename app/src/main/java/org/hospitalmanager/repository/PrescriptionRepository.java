package org.hospitalmanager.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.hospitalmanager.dto.MedicineWithId;
import org.hospitalmanager.dto.PrescriptionWithId;
import org.hospitalmanager.model.Medicine;
import org.hospitalmanager.model.Prescription;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PrescriptionRepository {

    PrescriptionWithId getPrescriptionById(String id) throws ExecutionException, InterruptedException;
    ArrayList<PrescriptionWithId> getPrescriptionByPatientId(String patientId) throws ExecutionException, InterruptedException;
    ArrayList<PrescriptionWithId> getPrescriptionByDoctorId(String doctorId) throws ExecutionException, InterruptedException;
    boolean createNewPrescription(Prescription prescription) throws ExecutionException, InterruptedException;
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
        Integer price = documentSnapshot.getLong("price").intValue(); // Firestore stores Integer as Long
        String note = documentSnapshot.getString("note");
        String instructions = documentSnapshot.getString("instructions");
        String diagnosis = documentSnapshot.getString("diagnosis");
        ArrayList<Medicine> medicineArrayList = documentSnapshot.get("medicineArrayList", ArrayList.class);

        return new Prescription(id, patientId, doctorId, dateCreated, expiryDate, price, note, instructions, diagnosis, medicineArrayList);
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

        if (!documentSnapshot.exists()) {
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
}
