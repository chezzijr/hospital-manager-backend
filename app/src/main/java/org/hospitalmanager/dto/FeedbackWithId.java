package org.hospitalmanager.dto;

import org.hospitalmanager.model.Feedback;

public class FeedbackWithId {

    private String id;
    private Feedback feedback;

    public FeedbackWithId(String id, Feedback feedback) {
        this.id = id;
        this.feedback = feedback;
    }

    public String getId() {
        return id;
    }

    public Feedback getFeedback() {
        return feedback;
    }
}
