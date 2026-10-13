package kigali.clinc.clinicmanagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import kigali.clinc.clinicmanagement.domain.Prescription;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, UUID> {
}