package org.hospitalmanager.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.hospitalmanager.dto.FeedbackWithId;
import org.hospitalmanager.model.Feedback;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;

public interface FeedbackRepository {

    ArrayList<FeedbackWithId> getAllFeedback() throws ExecutionException, InterruptedException;

    ArrayList<FeedbackWithId> getFeedbackByPatientId(String patientId) throws ExecutionException, InterruptedException;

    ArrayList<FeedbackWithId> getFeedbackByDoctorId(String doctorId) throws ExecutionException, InterruptedException;

    FeedbackWithId getFeedbackById(String id) throws ExecutionException, InterruptedException;

    boolean editFeedBackById(FeedbackWithId feedback) throws ExecutionException, InterruptedException;

    boolean createNewFeedback(Feedback feedback) throws ExecutionException, InterruptedException;
}

@Repository
class FeedbackRepositoryImpl implements FeedbackRepository {

    private final Firestore firestore = FirestoreClient.getFirestore();

    private Feedback convertDocumentSnapshotToFeedbackClass(DocumentSnapshot documentSnapshot) {
        String id = documentSnapshot.getString("id");
        String patientId = documentSnapshot.getString("patientId");
        String doctorId = documentSnapshot.getString("doctorId");
        String content = documentSnapshot.getString("content");
        Integer ratingStar = Objects.requireNonNull(documentSnapshot.getDouble("ratingStar")).intValue();
        Date dateCreated = documentSnapshot.getDate("dateCreated");

        return new Feedback(id, patientId, doctorId, content, ratingStar, dateCreated);
    }

    @Override
    public ArrayList<FeedbackWithId> getAllFeedback() throws ExecutionException, InterruptedException {
        ArrayList<FeedbackWithId> feedbackList = new ArrayList<>();

        ApiFuture<QuerySnapshot> query = firestore.collection("feedback").get();
        QuerySnapshot querySnapshot = query.get();

        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {

            String id = documentSnapshot.getId();
            Feedback feedback = convertDocumentSnapshotToFeedbackClass(documentSnapshot);
            feedbackList.add(new FeedbackWithId(id, feedback));
        }

        return feedbackList;
    }

    @Override
    public ArrayList<FeedbackWithId> getFeedbackByPatientId(String patientId) throws ExecutionException, InterruptedException {
        ArrayList<FeedbackWithId> feedbackList = new ArrayList<>();

        CollectionReference feedbackCollection = firestore.collection("feedback");

        Query query = feedbackCollection.whereEqualTo("patientId", patientId);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> queryDocumentSnapshots = querySnapshot.get().getDocuments();

        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
            String id = queryDocumentSnapshot.getId();
            Feedback feedback = convertDocumentSnapshotToFeedbackClass((DocumentSnapshot) queryDocumentSnapshot);
            feedbackList.add(new FeedbackWithId(id, feedback));
        }

        return feedbackList;
    }

    @Override
    public ArrayList<FeedbackWithId> getFeedbackByDoctorId(String doctorId) throws ExecutionException, InterruptedException {
        ArrayList<FeedbackWithId> feedbackList = new ArrayList<>();

        CollectionReference feedbackCollection = firestore.collection("feedback");

        Query query = feedbackCollection.whereEqualTo("doctorId", doctorId);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> queryDocumentSnapshots = querySnapshot.get().getDocuments();

        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
            String id = queryDocumentSnapshot.getId();
            Feedback feedback = convertDocumentSnapshotToFeedbackClass((DocumentSnapshot) queryDocumentSnapshot);
            feedbackList.add(new FeedbackWithId(id, feedback));
        }

        return feedbackList;
    }

    @Override
    public FeedbackWithId getFeedbackById(String feedbackId) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("feedback").document(feedbackId);

        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
        if (documentSnapshot.exists()) {
            String id = documentSnapshot.getId();
            Feedback feedback = convertDocumentSnapshotToFeedbackClass(documentSnapshot);
            return new FeedbackWithId(id, feedback);
        }

        return null;
    }

    private boolean documentExists(String documentId) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("feedback").document(documentId);
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
        return documentSnapshot.exists();
    }

    @Override
    public boolean editFeedBackById(FeedbackWithId feedback) throws ExecutionException, InterruptedException {
        if (!documentExists(feedback.getId())) {
            System.out.println("Feedback with id " + feedback.getId() + " does not exist.");
            return false;
        }

        DocumentReference feedbackRef = firestore.collection("feedback").document(feedback.getId());

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("patientId", feedback.getFeedback().getPatientId());
        updatedData.put("doctorId", feedback.getFeedback().getDoctorId());
        updatedData.put("content", feedback.getFeedback().getContent());
        updatedData.put("ratingStar", feedback.getFeedback().getRatingStar());

        feedbackRef.update(updatedData).get();

        return true;
    }

    @Override
    public boolean createNewFeedback(Feedback feedback) throws ExecutionException, InterruptedException {
        if (documentExists(feedback.getId())) {
            System.out.println("Feedback with id " + feedback.getId() + " does exist.");
            return false;
        }

        // Add feedback to feedbacks collection
        CollectionReference feedbackCollection = firestore.collection("feedback");
        feedbackCollection.document(feedback.getId()).set(feedback);
        System.out.println("Feedback created successfully.");
        return true;
    }
}
