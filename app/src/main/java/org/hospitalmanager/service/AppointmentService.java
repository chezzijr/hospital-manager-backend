package org.hospitalmanager.service;

import org.hospitalmanager.dto.AppointmentWithId;
import org.hospitalmanager.model.Appointment;
import org.hospitalmanager.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public interface AppointmentService {

    ArrayList<AppointmentWithId> getAllAppointment() throws ExecutionException, InterruptedException;

    ArrayList<AppointmentWithId> getAllAppointmentByPatientId(String patientId) throws ExecutionException, InterruptedException;

    ArrayList<AppointmentWithId> getAllAppointmentByDoctorId(String doctorId) throws ExecutionException, InterruptedException;
    boolean createAppointment(Appointment appointment);

    boolean isAppointmentBelongToPatient(String patientId, String appointmentId) throws ExecutionException, InterruptedException;

    boolean deleteAppointmentById(String appointmentId) throws ExecutionException, InterruptedException;

    AppointmentWithId getAppointmentById(String id) throws ExecutionException, InterruptedException;
}

@Service
class AppointmentServiceImpl implements AppointmentService {

    private AppointmentRepository appointmentRepository;

    @Autowired
    public void AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public ArrayList<AppointmentWithId> getAllAppointment() throws ExecutionException, InterruptedException {
        return appointmentRepository.getAllAppointment();
    }

    public ArrayList<AppointmentWithId> getAllAppointmentByPatientId(String patientId) throws ExecutionException, InterruptedException {
        return appointmentRepository.getAllAppointmentByPatientId(patientId);
    }

    public ArrayList<AppointmentWithId> getAllAppointmentByDoctorId(String doctorId) throws ExecutionException, InterruptedException {
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
    public AppointmentWithId getAppointmentById(String id) throws ExecutionException, InterruptedException {
        return appointmentRepository.getAppointmentById(id);
    }
}
