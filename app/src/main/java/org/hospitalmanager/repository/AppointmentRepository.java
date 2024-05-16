package org.hospitalmanager.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.hospitalmanager.dto.AppointmentWithId;
import org.hospitalmanager.model.Appointment;
import org.hospitalmanager.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;

public interface AppointmentRepository {

    ArrayList<AppointmentWithId> getAllAppointment() throws ExecutionException, InterruptedException;

    ArrayList<AppointmentWithId> getAllAppointmentByPatientId(String patientId) throws ExecutionException, InterruptedException;

    ArrayList<AppointmentWithId> getAllAppointmentByDoctorId(String doctorId) throws ExecutionException, InterruptedException;

    boolean createAppointment(Appointment appointment);

    boolean isAppointmentBelongToPatient(String patientId, String appointmentId) throws ExecutionException, InterruptedException;

    boolean deleteAppointmentById(String appointmentId) throws ExecutionException, InterruptedException;

    AppointmentWithId getAppointmentById(String id) throws ExecutionException, InterruptedException;

    ArrayList<AppointmentWithId> getAppointmentByRangeDay(Date startDate, Date endDate) throws ExecutionException, InterruptedException;
}

@Repository
class AppointmentRepositoryImpl implements AppointmentRepository {

    @Autowired
    private Firestore firestore;

    private Appointment convertDocumentSnapshotToAppointmentClass(DocumentSnapshot documentSnapshot) {
        String id = documentSnapshot.getString("id");
        String patientId = documentSnapshot.getString("patientId");
        Date appointmentDate = documentSnapshot.getDate("appointmentDate");
        String doctorId = documentSnapshot.getString("doctorId");
        String content = documentSnapshot.getString("content");

        Map<String, Object> data = documentSnapshot.getData();

        assert data != null;
        Map<String, Object> locationData = (Map<String, Object>) data.get("location");
        String address = (String) locationData.get("address");
        String floor = (String) locationData.get("floor");
        String roomNumber = (String) locationData.get("roomNumber");

        String status = documentSnapshot.getString("status");

        Location location = new Location(address, floor, roomNumber);
        Date dateCreated = documentSnapshot.getDate("dateCreated");

        return new Appointment(id, patientId, appointmentDate, doctorId, content, status, location, dateCreated);
    }

    public ArrayList<AppointmentWithId> getAllAppointment() throws ExecutionException, InterruptedException {
        ArrayList<AppointmentWithId> appointmentList = new ArrayList<>();

        ApiFuture<QuerySnapshot> query = firestore.collection("appointments").get();
        QuerySnapshot querySnapshot = query.get();

        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {

            String id = documentSnapshot.getId();
            Appointment appointment = convertDocumentSnapshotToAppointmentClass(documentSnapshot);
            appointmentList.add(new AppointmentWithId(id, appointment));
        }

        return appointmentList;
    }

    public ArrayList<AppointmentWithId> getAllAppointmentByPatientId(String patientId) throws ExecutionException, InterruptedException {
        ArrayList<AppointmentWithId> appointmentList = new ArrayList<>();

        CollectionReference appointmentsCollection = firestore.collection("appointments");

        Query query = appointmentsCollection.whereEqualTo("patientId", patientId);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> queryDocumentSnapshots = querySnapshot.get().getDocuments();

        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
            String id = queryDocumentSnapshot.getId();
            Appointment appointment = convertDocumentSnapshotToAppointmentClass((DocumentSnapshot) queryDocumentSnapshot);
            appointmentList.add(new AppointmentWithId(id, appointment));
        }

        return appointmentList;
    }

    public ArrayList<AppointmentWithId> getAllAppointmentByDoctorId(String doctorId) throws ExecutionException, InterruptedException {
        ArrayList<AppointmentWithId> appointmentList = new ArrayList<>();

        CollectionReference appointmentsCollection = firestore.collection("appointments");

        Query query = appointmentsCollection.whereEqualTo("doctorId", doctorId);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> queryDocumentSnapshots = querySnapshot.get().getDocuments();

        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
            String id = queryDocumentSnapshot.getId();
            Appointment appointment = convertDocumentSnapshotToAppointmentClass((DocumentSnapshot) queryDocumentSnapshot);
            appointmentList.add(new AppointmentWithId(id, appointment));
        }

        return appointmentList;
    }


    public boolean createAppointment(Appointment appointment) {
        CollectionReference appointmentsCollection = firestore.collection("appointments");
        appointmentsCollection.document(appointment.getId()).set(appointment);
        System.out.println("Appointment created successfully.");
        return true;
    }

    public boolean isAppointmentBelongToPatient(String patientId, String appointmentId) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("appointment").document(appointmentId);

        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
        if (documentSnapshot.exists()) {
             Appointment appointment = convertDocumentSnapshotToAppointmentClass(documentSnapshot);
            return Objects.equals(appointment.getPatientId(), patientId);
        }
        return false;
    }

    public boolean deleteAppointmentById(String appointmentId) throws ExecutionException, InterruptedException {
//        DocumentReference documentReference = firestore.collection("appointments").document(appointmentId);
//        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
//        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
//
//        if (documentSnapshot.exists()) {
//            // Document exists, delete it
//            documentReference.delete();
//            System.out.println("Appointment with id " + appointmentId + " deleted successfully.");
//            return true;
//        } else {
//            // Document does not exist
//            System.out.println("Appointment with id " + appointmentId + " does not exist.");
//            return false;
//        }

        return false;
    }

    public AppointmentWithId getAppointmentById(String appointmentId) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("appointments").document(appointmentId);

        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
        if (documentSnapshot.exists()) {
            String id = documentSnapshot.getId();
            Appointment appointment = convertDocumentSnapshotToAppointmentClass(documentSnapshot);
            return new AppointmentWithId(id, appointment);
        }

        return null;
    }

    @Override
    public ArrayList<AppointmentWithId> getAppointmentByRangeDay(Date startDate, Date endDate) throws ExecutionException, InterruptedException {
        Query query = firestore.collection("appointment")
                .whereGreaterThanOrEqualTo("appointmentDate", startDate)
                .whereLessThanOrEqualTo("appointmentDate", endDate);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        ArrayList<AppointmentWithId> appointmentList = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            String id = document.getId();
            Appointment appointment = convertDocumentSnapshotToAppointmentClass(document);
            appointmentList.add(new AppointmentWithId(id, appointment));
        }
        return appointmentList;
    }
}
