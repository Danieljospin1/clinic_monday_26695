package kigali.clinc.clinicmanagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import kigali.clinc.clinicmanagement.domain.Prescription;

public interface PrescriptionRepository
                extends JpaRepository<Prescription, UUID> {
        List<Prescription> findByAppointment_Id(UUID appointmentId);
}