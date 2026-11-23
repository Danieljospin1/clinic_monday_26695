package kigali.clinc.clinicmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.domain.Prescription;
import kigali.clinc.clinicmanagement.repository.AppointmentRepository;
import kigali.clinc.clinicmanagement.repository.PrescriptionRepository;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    public PrescriptionService(
            PrescriptionRepository prescriptionRepository,
            AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public String savePrescription(Prescription prescription, UUID appointmentId) {

        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isEmpty()) {
            return "The appointment with that id does not exist";
        }

        Appointment appointment = optionalAppointment.get();

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            return "Prescriptions are only for COMPLETED appointments";
        }

        prescription.setAppointment(appointment);
        prescription.setIssuedDate(LocalDate.now());

        prescriptionRepository.save(prescription);

        return "Prescription saved successfully";
    }

    public List<Prescription> getPrescriptionHistory(String ssNumber) {
        return prescriptionRepository
                .findPrescriptionHistoryByPatientSsNumber(ssNumber);
    }

    public Optional<List<Prescription>> getPrescriptionsByAppointmentId(
            UUID appointmentId) {

        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);

        if (appointment.isEmpty()) {
            return Optional.empty();
        }

        List<Prescription> prescriptions = prescriptionRepository.findByAppointment_Id(appointmentId);

        return Optional.of(prescriptions);
    }

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Optional<Prescription> getPrescriptionById(UUID id) {
        return prescriptionRepository.findById(id);
    }

    public String deletePrescription(UUID id) {
        if (!prescriptionRepository.existsById(id)) {
            return "The prescription with that id does not exist";
        }

        prescriptionRepository.deleteById(id);
        return "Prescription deleted successfully";
    }

    public String updatePrescription(UUID id, Prescription updatedPrescription) {

        Optional<Prescription> optionalPrescription = prescriptionRepository.findById(id);

        if (optionalPrescription.isEmpty()) {
            return "The prescription with that id does not exist";
        }

        Prescription existingPrescription = optionalPrescription.get();

        existingPrescription.setMedicineName(
                updatedPrescription.getMedicineName());

        existingPrescription.setDosage(
                updatedPrescription.getDosage());

        existingPrescription.setDurationDays(
                updatedPrescription.getDurationDays());

        prescriptionRepository.save(existingPrescription);

        return "Prescription updated successfully";
    }

    

}