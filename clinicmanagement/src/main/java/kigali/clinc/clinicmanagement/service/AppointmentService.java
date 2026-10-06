package kigali.clinc.clinicmanagement.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.repository.AppointmentRepository;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // Save appointment with check
    public String saveAppointment(Appointment appointment) {

        boolean doctorAlreadyBooked = appointmentRepository.existsByDoctorIdAndAppointmentDateAndStatusNot(
                appointment.getDoctor().getId(),
                appointment.getAppointmentDate(),
                AppointmentStatus.CANCELLED);

        System.out.println("Doctor ID: " + appointment.getDoctor().getId());
        System.out.println("Appointment date: " + appointment.getAppointmentDate());
        System.out.println("Already booked: " + doctorAlreadyBooked);

        if (doctorAlreadyBooked) {
            return "Doctor is already booked on that date";
        }

        appointmentRepository.save(appointment);

        return "Appointment saved successfully";
    }

    public List<Object[]> countAppointmentsByStatus() {
        return appointmentRepository.countAppointmentsByStatus();
    }

    // Get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // appointment by status
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository
                .findByStatusOrderByAppointmentDateAsc(status);
    }

    // Get appointment by ID
    public Appointment getAppointmentById(UUID id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
    }

    public List<Appointment> getAppointmentsBetweenDates(
            Date start,
            Date end) {

        return appointmentRepository
                .findByAppointmentDateBetweenOrderByAppointmentDateAsc(
                        start, end);
    }

    // Update appointment
    public Appointment updateAppointment(UUID id, Appointment appointmentDetails) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        appointment.setAppointmentDate(appointmentDetails.getAppointmentDate());
        appointment.setReason(appointmentDetails.getReason());
        appointment.setStatus(appointmentDetails.getStatus());
        appointment.setPatient(appointmentDetails.getPatient());
        appointment.setDoctor(appointmentDetails.getDoctor());

        return appointmentRepository.save(appointment);
    }

    // Delete appointment
    public void deleteAppointment(UUID id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        appointmentRepository.delete(appointment);
    }

    // Get appointments by doctor
    public List<Appointment> getAppointmentsByDoctor(UUID doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    // Get appointments by doctor ordered by date ascending
    public List<Appointment> getAppointmentsByDoctorOrderedByDate(UUID doctorId) {
        return appointmentRepository
                .findByDoctorIdOrderByAppointmentDateAsc(doctorId);
    }

    // Get appointments by doctor ordered by date descending
    public List<Appointment> getAppointmentsByDoctorOrderedByDateDesc(UUID doctorId) {
        return appointmentRepository
                .findByDoctorIdOrderByAppointmentDateDesc(doctorId);
    }

    // Get number of appointments for a doctor
    public long getAppointmentCountByDoctor(UUID doctorId) {
        return appointmentRepository.countByDoctorId(doctorId);
    }
}