package org.hospitalmanager.dto;

import org.hospitalmanager.model.Appointment;

public class AppointmentWithId {

    private final String id;
    private final Appointment appointment;

    public AppointmentWithId(String id, Appointment appointment) {
        this.id = id;
        this.appointment = appointment;
    }

    public String getId() {
        return id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

}
