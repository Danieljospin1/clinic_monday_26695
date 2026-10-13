package kigali.clinc.clinicmanagement.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    // Get appointments for a specific doctor
    List<Appointment> findByDoctorId(UUID doctorId);

    // Get appointments for a doctor ordered by date ascending
    List<Appointment> findByDoctorIdOrderByAppointmentDateAsc(UUID doctorId);

    // Get appointments for a doctor ordered by date descending
    List<Appointment> findByDoctorIdOrderByAppointmentDateDesc(UUID doctorId);

    boolean existsByDoctorIdAndAppointmentDateAndStatusNotAndIdNot(
            UUID doctorId,
            java.util.Date appointmentDate,
            AppointmentStatus status,
            UUID id);

    // Count appointments for a specific doctor
    long countByDoctorId(UUID doctorId);

    // find by appointment status
    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status);

    // find By Appointment Date Between Order By Appointment Date
    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(
            Date start,
            Date end);

    // conflicting appointment exists before saving
    boolean existsByDoctorIdAndAppointmentDateAndStatusNot(
            UUID doctorId,
            Date appointmentDate,
            AppointmentStatus status);

    @Query("""
                SELECT a.status, COUNT(a)
                FROM Appointment a
                GROUP BY a.status
            """)
    List<Object[]> countAppointmentsByStatus();

    @Modifying
    @Query("""
                UPDATE Appointment a
                SET a.status = AppointmentStatus.CANCELLED
                WHERE a.doctor.id = :doctorId
                  AND a.appointmentDate = :date
                  AND a.status <> AppointmentStatus.COMPLETED
            """)
    int cancelAppointmentsForDoctorOnDate(
            @Param("doctorId") UUID doctorId,
            @Param("date") Date date);

    List<Appointment> findByDoctor_IdAndAppointmentDateAndStatusNot(
            UUID doctorId,
            Date appointmentDate,
            AppointmentStatus status);
}