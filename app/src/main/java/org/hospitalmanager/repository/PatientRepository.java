package org.hospitalmanager.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.hospitalmanager.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.hospitalmanager.model.User.Role.PATIENT;

public interface PatientRepository {

    public Patient getPatientById(String patientId) throws ExecutionException, InterruptedException;

    public ArrayList<Patient> getAllPatient() throws ExecutionException, InterruptedException;
}

@Repository
class PatientRepositoryImpl implements PatientRepository {

    private final Firestore firestore = FirestoreClient.getFirestore();

    @Override
    public Patient getPatientById(String patientId) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("patient").document(patientId);

        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
        if (documentSnapshot.exists()) {

            String id = documentSnapshot.getString("id");
            String email = documentSnapshot.getString("email");
            String gender = documentSnapshot.getString("gender");
            Date dateOfBirth = documentSnapshot.getDate("dateOfBirth");

            return new Patient(id, email, PATIENT, "**********", gender, dateOfBirth);
        }

        return null;
    }

    @Override
    public ArrayList<Patient> getAllPatient() throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("patient").document();
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();

        ArrayList<Patient> patientList = new ArrayList<>();

        if (documentSnapshot.exists()) {
            Map<String, Object> patientsData = documentSnapshot.getData();
            assert patientsData != null;
            for (Map.Entry<String, Object> entry : patientsData.entrySet()) {
                Patient patient = getPatient(entry);
                patientList.add(patient);
            }
        }

        return patientList;
    }

    private static Patient getPatient(Map.Entry<String, Object> entry) {
        Map<String, Object> patientData = (Map<String, Object>) entry.getValue();

        String id = (String) patientData.get("id");
        String email = (String) patientData.get("email");
        String gender = (String) patientData.get("gender");
        Date dateOfBirth = (Date) patientData.get("dateOfBirth");

        return new Patient(id, email, PATIENT, "**********", gender, dateOfBirth);
    }

}
