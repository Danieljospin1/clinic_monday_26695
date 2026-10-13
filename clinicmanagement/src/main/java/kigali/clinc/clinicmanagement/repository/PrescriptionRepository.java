package kigali.clinc.clinicmanagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kigali.clinc.clinicmanagement.domain.Prescription;

public interface PrescriptionRepository
                extends JpaRepository<Prescription, UUID> {
        List<Prescription> findByAppointment_Id(UUID appointmentId);

        @Query("""
                        SELECT p
                        FROM Prescription p
                        JOIN p.appointment a
                        JOIN a.patient patient
                        WHERE patient.ssNumber = :ssNumber
                        ORDER BY p.issuedDate DESC
                        """)
        List<Prescription> findPrescriptionHistoryByPatientSsNumber(
                        @Param("ssNumber") String ssNumber);
}