package org.hospitalmanager.service;

import org.hospitalmanager.dto.FeedbackWithId;
import org.hospitalmanager.model.Feedback;
import org.hospitalmanager.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public interface FeedbackService {

    ArrayList<FeedbackWithId> getAllFeedback() throws ExecutionException, InterruptedException;

    ArrayList<FeedbackWithId> getFeedbackByPatientId(String patientId) throws ExecutionException, InterruptedException;

    ArrayList<FeedbackWithId> getFeedbackByDoctorId(String doctorId) throws ExecutionException, InterruptedException;

    FeedbackWithId getFeedbackById(String id) throws ExecutionException, InterruptedException;

    boolean editFeedBackById(FeedbackWithId feedback) throws ExecutionException, InterruptedException;

    boolean createNewFeedback(Feedback feedback) throws ExecutionException, InterruptedException;

}

@Service
class FeedbackServiceImpl implements FeedbackService {

    private FeedbackRepository feedbackRepository;

    @Autowired
    public void setFeedbackRepository(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public ArrayList<FeedbackWithId> getAllFeedback() throws ExecutionException, InterruptedException {
        return feedbackRepository.getAllFeedback();
    }

    @Override
    public ArrayList<FeedbackWithId> getFeedbackByPatientId(String patientId) throws ExecutionException, InterruptedException {
        return feedbackRepository.getFeedbackByPatientId(patientId);
    }

    @Override
    public ArrayList<FeedbackWithId> getFeedbackByDoctorId(String doctorId) throws ExecutionException, InterruptedException {
        return feedbackRepository.getFeedbackByDoctorId(doctorId);
    }

    @Override
    public FeedbackWithId getFeedbackById(String id) throws ExecutionException, InterruptedException {
        return feedbackRepository.getFeedbackById(id);
    }

    @Override
    public boolean editFeedBackById(FeedbackWithId feedback) throws ExecutionException, InterruptedException {
        return feedbackRepository.editFeedBackById(feedback);
    }

    @Override
    public boolean createNewFeedback(Feedback feedback) throws ExecutionException, InterruptedException {
        return feedbackRepository.createNewFeedback(feedback);
    }

}
