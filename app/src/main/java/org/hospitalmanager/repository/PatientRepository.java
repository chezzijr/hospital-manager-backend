package org.hospitalmanager.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.hospitalmanager.dto.PaitentWithId;
import org.hospitalmanager.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static org.hospitalmanager.model.User.Role.PATIENT;

public interface PatientRepository {

    PaitentWithId getPatientById(String patientId) throws ExecutionException, InterruptedException;

    ArrayList<PaitentWithId> getAllPatient() throws ExecutionException, InterruptedException;

    boolean createNewPatient(Patient patient) throws ExecutionException, InterruptedException;
}

@Repository
class PatientRepositoryImpl implements PatientRepository {

    private final Firestore firestore = FirestoreClient.getFirestore();

    private Patient convertDocumentSnapshotToPatientClass(DocumentSnapshot documentSnapshot) {
        String id = documentSnapshot.getString("id");
        String email = documentSnapshot.getString("email");
        String gender = documentSnapshot.getString("gender");
        Date dateOfBirth = documentSnapshot.getDate("dateOfBirth");

        return new Patient(id, email, PATIENT, "**********", gender, dateOfBirth);
    }

    @Override
    public PaitentWithId getPatientById(String patientId) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("patient").document(patientId);

        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
        if (documentSnapshot.exists()) {
            String id = documentSnapshot.getId();
            Patient patient = convertDocumentSnapshotToPatientClass(documentSnapshot);
            return new PaitentWithId(id, patient);
        }

        return null;
    }

    @Override
    public ArrayList<PaitentWithId> getAllPatient() throws ExecutionException, InterruptedException {
        ArrayList<PaitentWithId> patientList = new ArrayList<>();

        ApiFuture<QuerySnapshot> query = firestore.collection("patient").get();
        QuerySnapshot querySnapshot = query.get();

        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
            String id = documentSnapshot.getId();
            Patient patient = convertDocumentSnapshotToPatientClass(documentSnapshot);
            patientList.add(new PaitentWithId(id, patient));
        }

        return patientList;
    }

    @Override
    public boolean createNewPatient(Patient patient) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("patient").document(patient.getId());
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();

        if (documentSnapshot.exists()) {
            System.out.println("Patient with id " + patient.getId() + "does exist.");
            return false;
        }
        else {

            CollectionReference patientCollection = firestore.collection("patient");
            patientCollection.document(patient.getId()).set(patient);
            System.out.println("Patient created successfully.");
            return true;
        }

    }

}
