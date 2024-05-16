package org.hospitalmanager.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.hospitalmanager.dto.DoctorWithId;
import org.hospitalmanager.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static org.hospitalmanager.model.User.Role.DOCTOR;

public interface DoctorRepository {

    DoctorWithId getDoctorById(String doctorId) throws ExecutionException, InterruptedException;

    ArrayList<DoctorWithId> getAllDoctor() throws ExecutionException, InterruptedException;

    boolean createNewDoctor(Doctor doctor);

    ArrayList<DoctorWithId> getDoctorByName(String name) throws ExecutionException, InterruptedException;

    ArrayList<DoctorWithId> getDoctorBySpecialization(String specialization) throws ExecutionException, InterruptedException;

}

@Repository
class DoctorRepositoryImpl implements DoctorRepository {

    @Autowired
    private Firestore firestore;

    private Doctor convertDocumentSnapshotToDoctorClass(DocumentSnapshot documentSnapshot) {
        String id = documentSnapshot.getString("id");
        String name = documentSnapshot.getString("name");
        String specialization = documentSnapshot.getString("specialization");
        String qualification = documentSnapshot.getString("qualification");
        Integer yearOfExperience = Objects.requireNonNull(documentSnapshot.getDouble("yearOfExperience")).intValue();
        Integer workingHours = Objects.requireNonNull(documentSnapshot.getDouble("workingHours")).intValue();
        String phoneNumber = documentSnapshot.getString("phoneNumber");
        String email = documentSnapshot.getString("email");
        String gender = documentSnapshot.getString("gender");
        Date dateOfBirth = documentSnapshot.getDate("dateOfBirth");
        String password = "**********";

        return new Doctor(id, email, DOCTOR, password, name, specialization, qualification, yearOfExperience, phoneNumber, workingHours, gender, dateOfBirth);
    }

    @Override
    public DoctorWithId getDoctorById(String doctorId) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("doctor").document(doctorId);

        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
        if (documentSnapshot.exists()) {
            String id = documentSnapshot.getId();
            Doctor doctor = convertDocumentSnapshotToDoctorClass(documentSnapshot);
            return new DoctorWithId(id, doctor);
        }

        return null;
    }

    @Override
    public ArrayList<DoctorWithId> getAllDoctor() throws ExecutionException, InterruptedException {
        ArrayList<DoctorWithId> doctorList = new ArrayList<>();

        ApiFuture<QuerySnapshot> query = firestore.collection("doctor").get();
        QuerySnapshot querySnapshot = query.get();

        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
            String id = documentSnapshot.getId();
            Doctor doctor = convertDocumentSnapshotToDoctorClass(documentSnapshot);
            doctorList.add(new DoctorWithId(id, doctor));
        }

        return doctorList;
    }


    private boolean documentExists(String documentId) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("doctor").document(documentId);
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
        return documentSnapshot.exists();
    }

    @Override
    public boolean createNewDoctor(Doctor doctor) {

        try {

            if (documentExists(doctor.getId())) {
                System.out.println("Doctor with id " + doctor.getId() + " already exists.");
                return false;
            }

            // Add doctor to doctor collection
            CollectionReference appointmentsCollection = firestore.collection("doctor");
            appointmentsCollection.document(doctor.getId()).set(doctor);
            System.out.println("Doctor created successfully.");
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }


    }

    @Override
    public ArrayList<DoctorWithId> getDoctorByName(String name) throws ExecutionException, InterruptedException {
        ArrayList<DoctorWithId> doctorList = new ArrayList<>();

        CollectionReference doctorCollection = firestore.collection("doctor");

        Query query = doctorCollection.whereEqualTo("name", name);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> queryDocumentSnapshots = querySnapshot.get().getDocuments();

        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
            String id = queryDocumentSnapshot.getId();
            Doctor doctor = convertDocumentSnapshotToDoctorClass((DocumentSnapshot) queryDocumentSnapshot);
            doctorList.add(new DoctorWithId(id, doctor));
        }

        return doctorList;
    }

    @Override
    public ArrayList<DoctorWithId> getDoctorBySpecialization(String specialization) throws ExecutionException, InterruptedException {
        ArrayList<DoctorWithId> doctorList = new ArrayList<>();

        CollectionReference doctorCollection = firestore.collection("doctor");

        Query query = doctorCollection.whereEqualTo("specialization", specialization);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> queryDocumentSnapshots = querySnapshot.get().getDocuments();

        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
            String id = queryDocumentSnapshot.getId();
            Doctor doctor = convertDocumentSnapshotToDoctorClass((DocumentSnapshot) queryDocumentSnapshot);
            doctorList.add(new DoctorWithId(id, doctor));
        }

        return doctorList;
    }



}
