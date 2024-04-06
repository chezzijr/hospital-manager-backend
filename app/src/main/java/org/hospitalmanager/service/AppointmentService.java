package org.hospitalmanager.service;

import org.hospitalmanager.model.Appointment;
import org.hospitalmanager.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public interface AppointmentService {

    ArrayList<Appointment> getAllAppointment() throws ExecutionException, InterruptedException;

    ArrayList<Appointment> getAllAppointmentByPatientId(String patientId) throws ExecutionException, InterruptedException;

    ArrayList<Appointment> getAllAppointmentByDoctorId(String doctorId) throws ExecutionException, InterruptedException;
    boolean createAppointment(Appointment appointment);

    boolean isAppointmentBelongToPatient(String patientId, String appointmentId) throws ExecutionException, InterruptedException;

    boolean deleteAppointmentById(String appointmentId) throws ExecutionException, InterruptedException;

    Appointment getAppointmentById(String id) throws ExecutionException, InterruptedException;
}

@Service
class AppointmentServiceImpl implements AppointmentService {

    private AppointmentRepository appointmentRepository;

    @Autowired
    public void AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public ArrayList<Appointment> getAllAppointment() throws ExecutionException, InterruptedException {
        return appointmentRepository.getAllAppointment();
    }

    public ArrayList<Appointment> getAllAppointmentByPatientId(String patientId) throws ExecutionException, InterruptedException {
        return appointmentRepository.getAllAppointmentByPatientId(patientId);
    }

    public ArrayList<Appointment> getAllAppointmentByDoctorId(String doctorId) throws ExecutionException, InterruptedException {
        return appointmentRepository.getAllAppointmentByDoctorId(doctorId);
    }


    @Override
    public boolean createAppointment(Appointment appointment) {
        return appointmentRepository.createAppointment(appointment);
    }

    @Override
    public boolean isAppointmentBelongToPatient(String patientId, String appointmentId) throws ExecutionException, InterruptedException {
        return appointmentRepository.isAppointmentBelongToPatient(patientId, appointmentId);
    }

    @Override
    public boolean deleteAppointmentById(String appointmentId) throws ExecutionException, InterruptedException {
        return appointmentRepository.deleteAppointmentById(appointmentId);
    }

    @Override
    public Appointment getAppointmentById(String id) throws ExecutionException, InterruptedException {
        return appointmentRepository.getAppointmentById(id);
    }
}
