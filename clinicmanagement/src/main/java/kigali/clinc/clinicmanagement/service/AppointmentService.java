package kigali.clinc.clinicmanagement.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        LocalDate appointmentDate = new java.sql.Date(
                appointment.getAppointmentDate().getTime()).toLocalDate();
        LocalDate today = LocalDate.now(ZoneId.systemDefault());

        if (appointmentDate.isBefore(today)) {
            return "Appointment date cannot be in the past";
        }

        boolean doctorAlreadyBooked = appointmentRepository.existsByDoctorIdAndAppointmentDateAndStatusNot(
                appointment.getDoctor().getId(),
                appointment.getAppointmentDate(),
                AppointmentStatus.CANCELLED);

        if (doctorAlreadyBooked) {
            return "Doctor is already booked on that date";
        }
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        appointmentRepository.save(appointment);

        return "Appointment saved successfully";
    }

    public List<Object[]> countAppointmentsByStatus() {
        return appointmentRepository.countAppointmentsByStatus();
    }

    @Transactional
    public String cancelAppointmentsForDoctorOnDate(UUID doctorId, Date date) {

        int cancelled = appointmentRepository.cancelAppointmentsForDoctorOnDate(
                doctorId,
                date);

        return cancelled + " appointments cancelled";
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

    public String updateAppointmentStatus(UUID id, AppointmentStatus newStatus) {

        Appointment appointment = appointmentRepository.findById(id).orElse(null);

        if (appointment == null) {
            return "The appointment with that id does not exist";
        }

        AppointmentStatus currentStatus = appointment.getStatus();

        boolean allowed = switch (currentStatus) {
            case SCHEDULED ->
                newStatus == AppointmentStatus.CONFIRMED
                        || newStatus == AppointmentStatus.CANCELLED;

            case CONFIRMED ->
                newStatus == AppointmentStatus.COMPLETED
                        || newStatus == AppointmentStatus.CANCELLED;

            case COMPLETED, CANCELLED -> false;
        };

        if (!allowed) {
            return "Cannot change status from " + currentStatus
                    + " to " + newStatus;
        }

        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);

        return "Appointment status updated successfully";
    }
}