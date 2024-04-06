package org.hospitalmanager.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.hospitalmanager.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static org.hospitalmanager.model.User.Role.PATIENT;

public interface PatientRepository {

    Patient getPatientById(String patientId) throws ExecutionException, InterruptedException;

    ArrayList<Patient> getAllPatient() throws ExecutionException, InterruptedException;
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
    public Patient getPatientById(String patientId) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("patient").document(patientId);

        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
        if (documentSnapshot.exists()) {

            return convertDocumentSnapshotToPatientClass(documentSnapshot);
        }

        return null;
    }

    @Override
    public ArrayList<Patient> getAllPatient() throws ExecutionException, InterruptedException {
        ArrayList<Patient> patientList = new ArrayList<>();

        ApiFuture<QuerySnapshot> query = firestore.collection("patient").get();
        QuerySnapshot querySnapshot = query.get();

        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {

            Patient patient = convertDocumentSnapshotToPatientClass(documentSnapshot);
            patientList.add(patient);
        }

        return patientList;
    }

}
