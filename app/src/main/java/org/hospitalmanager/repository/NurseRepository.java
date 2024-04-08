package org.hospitalmanager.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.hospitalmanager.dto.NurseWithId;
import org.hospitalmanager.model.Nurse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static org.hospitalmanager.model.User.Role.NURSE;

public interface NurseRepository {

    ArrayList<NurseWithId> getAllNurse() throws ExecutionException, InterruptedException;

    NurseWithId getNurseById(String id) throws ExecutionException, InterruptedException;

    ArrayList<NurseWithId> getNurseByDepartment(String department) throws ExecutionException, InterruptedException;

    boolean addNewNurse(Nurse nurse) throws ExecutionException, InterruptedException;

}

@Repository
class NurRepositoryImpl implements NurseRepository {

    private final Firestore firestore = FirestoreClient.getFirestore();

    private Nurse convertDocumentSnapshotToNurseClass(DocumentSnapshot documentSnapshot) {
        String id = documentSnapshot.getString("id");
        String email = documentSnapshot.getString("email");
        String name = documentSnapshot.getString("name");
        String password = documentSnapshot.getString("password");
        String phoneNumber = documentSnapshot.getString("phoneNumber");
        Integer yearOfExperience = Objects.requireNonNull(documentSnapshot.getDouble("yearOfExperience")).intValue();
        Integer workingHours = Objects.requireNonNull(documentSnapshot.getDouble("workingHours")).intValue();
        String gender = documentSnapshot.getString("gender");
        Date dateOfBirth = documentSnapshot.getDate("dateOfBirth");
        String department = documentSnapshot.getString("department");

        return new Nurse(id, email, NURSE, password, name, phoneNumber, yearOfExperience, workingHours, gender, dateOfBirth, department);

    }

    @Override
    public ArrayList<NurseWithId> getAllNurse() throws ExecutionException, InterruptedException {
        ArrayList<NurseWithId> nurseList = new ArrayList<>();

        ApiFuture<QuerySnapshot> query = firestore.collection("nurse").get();
        QuerySnapshot querySnapshot = query.get();

        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
            String id = documentSnapshot.getId();
            Nurse nurse = convertDocumentSnapshotToNurseClass(documentSnapshot);
            nurseList.add(new NurseWithId(id, nurse));
        }

        return nurseList;
    }

    @Override
    public NurseWithId getNurseById(String nurseId) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("nurse").document(nurseId);

        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
        if (documentSnapshot.exists()) {
            String id = documentSnapshot.getId();
            Nurse nurse = convertDocumentSnapshotToNurseClass(documentSnapshot);
            return new NurseWithId(id, nurse);
        }

        return null;
    }

    @Override
    public ArrayList<NurseWithId> getNurseByDepartment(String department) throws ExecutionException, InterruptedException {
        ArrayList<NurseWithId> nurseList = new ArrayList<>();

        CollectionReference nurseCollection = firestore.collection("nurse");

        Query query = nurseCollection.whereEqualTo("department", department);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> queryDocumentSnapshots = querySnapshot.get().getDocuments();

        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
            String id = queryDocumentSnapshot.getId();
            Nurse nurse = convertDocumentSnapshotToNurseClass((DocumentSnapshot) queryDocumentSnapshot);
            nurseList.add(new NurseWithId(id, nurse));
        }

        return nurseList;
    }

    @Override
    public boolean addNewNurse(Nurse nurse) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("nurse").document(nurse.getId());
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();

        if (documentSnapshot.exists()) {
            System.out.println("Nurse with id " + nurse.getId() + "does exist.");
            return false;
        }
        else {

            CollectionReference appointmentsCollection = firestore.collection("nurse");
            appointmentsCollection.document(nurse.getId()).set(nurse);

            System.out.println("Nurse created successfully.");
            return true;
        }
    }
}
