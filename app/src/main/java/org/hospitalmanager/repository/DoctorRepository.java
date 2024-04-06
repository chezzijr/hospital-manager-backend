package org.hospitalmanager.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.hospitalmanager.model.Doctor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static org.hospitalmanager.model.User.Role.DOCTOR;

public interface DoctorRepository {

    Doctor getDoctorById(String doctorId) throws ExecutionException, InterruptedException;

    ArrayList<Doctor> getAllDoctor() throws ExecutionException, InterruptedException;

//    public ArrayList<Doctor> getDoctorByName(String name) throws ExecutionException, InterruptedException;

//    public ArrayList<Doctor> getDoctorBySpecialization(String specialization) throws ExecutionException, InterruptedException;

}

@Repository
class DoctorRepositoryImpl implements DoctorRepository {

    private final Firestore firestore = FirestoreClient.getFirestore();

    private Doctor convertDocumentSnapshotToDoctorClass(DocumentSnapshot documentSnapshot) {
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

    public Doctor getDoctorById(String doctorId) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("doctor").document(doctorId);

        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
        if (documentSnapshot.exists()) {

            return convertDocumentSnapshotToDoctorClass(documentSnapshot);
        }

        return null;
    }

    public ArrayList<Doctor> getAllDoctor() throws ExecutionException, InterruptedException {
        ArrayList<Doctor> doctorList = new ArrayList<>();

        ApiFuture<QuerySnapshot> query = firestore.collection("doctor").get();
        QuerySnapshot querySnapshot = query.get();

        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {

            Doctor doctor = convertDocumentSnapshotToDoctorClass(documentSnapshot);
            doctorList.add(doctor);
        }

        return doctorList;
    }

//    public ArrayList<Doctor> getDoctorBySpecialization(String specialization) throws ExecutionException, InterruptedException {
//
//    }

}
