package org.hospitalmanager.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.hospitalmanager.model.Doctor;
import org.hospitalmanager.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static org.hospitalmanager.model.User.Role.DOCTOR;
import static org.hospitalmanager.model.User.Role.PATIENT;

public interface DoctorRepository {

    public Doctor getDoctorById(String doctorId) throws ExecutionException, InterruptedException;

    public ArrayList<Doctor> getAllDoctor() throws ExecutionException, InterruptedException;

}

@Repository
class DoctorRepositoryImpl implements DoctorRepository {

    Firestore firestore = FirestoreClient.getFirestore();

    public Doctor getDoctorById(String doctorId) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("doctor").document(doctorId);

        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
        if (documentSnapshot.exists()) {

            String id = documentSnapshot.getString("id");
            String name = documentSnapshot.getString("name");
            String specialization = documentSnapshot.getString("specialization");
            String qualification = documentSnapshot.getString("qualification");
            Integer yearOfExperience = Objects.requireNonNull(documentSnapshot.getDouble("yearOfExperience")).intValue();
            Integer workingHours = Integer.valueOf(Objects.requireNonNull(documentSnapshot.getString("workingHours")));
            String phoneNumber = documentSnapshot.getString("phoneNumber");
            String email = documentSnapshot.getString("email");
            String gender = documentSnapshot.getString("gender");
            Date dateOfBirth = documentSnapshot.getDate("dateOfBirth");
            String password = "**********";

            return new Doctor(id, email, DOCTOR, password, name, specialization, qualification, yearOfExperience, phoneNumber, workingHours, gender, dateOfBirth);
        }

        return null;
    }

    public ArrayList<Doctor> getAllDoctor() throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("doctor").document();
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();

        ArrayList<Doctor> doctorList = new ArrayList<>();

        if (documentSnapshot.exists()) {
            Map<String, Object> doctorData = documentSnapshot.getData();
            assert doctorData != null;
            for (Map.Entry<String, Object> entry : doctorData.entrySet()) {
                Doctor doctor = getDoctor(entry);
                doctorList.add(doctor);
            }
        }

        return doctorList;
    }

    private static Doctor getDoctor(Map.Entry<String, Object> entry) {
        Map<String, Object> doctorData = (Map<String, Object>) entry.getValue();

        String id = (String) doctorData.get("id");
        String name = (String) doctorData.get("name");
        String specialization = (String) doctorData.get("specialization");
        String qualification = (String) doctorData.get("qualification");
        Integer yearOfExperience = (Integer) doctorData.get("yearOfExperience");
        Integer workingHours = (Integer) doctorData.get("workingHours");
        String phoneNumber = (String) doctorData.get("phoneNumber");
        String email = (String) doctorData.get("email");
        String gender = (String) doctorData.get("gender");
        Date dateOfBirth = (Date) doctorData.get("dateOfBirth");
        String password = "**********";

        return new Doctor(id, email, DOCTOR, password, name, specialization, qualification, yearOfExperience, phoneNumber, workingHours, gender, dateOfBirth);
    }
}
