package kigali.clinc.clinicmanagement.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.LinkedHashMap;
import java.sql.*;
import java.time.LocalDate;
import java.util.Locale;
import kigali.clinc.clinicmanagement.repository.AppointmentRepository;
import kigali.clinc.clinicmanagement.repository.DoctorRepository;
import kigali.clinc.clinicmanagement.repository.PatientRepository;
import kigali.clinc.clinicmanagement.repository.PrescriptionRepository;
import kigali.clinc.clinicmanagement.repository.OfficeRepository;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
@Service
public class DashboardService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final OfficeRepository officeRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;

    public DashboardService(
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            OfficeRepository officeRepository,
            AppointmentRepository appointmentRepository,
            PrescriptionRepository prescriptionRepository) {

        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.officeRepository = officeRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    public Map<String, Long> getSummary() {

        Map<String, Long> summary = new LinkedHashMap<>();

        java.sql.Date today =
                java.sql.Date.valueOf(LocalDate.now());

        summary.put("patients", patientRepository.count());
        summary.put("doctors", doctorRepository.count());
        summary.put("freeOffices", officeRepository.countByDoctorIsNull());
        summary.put("appointmentsToday",
                appointmentRepository.countByAppointmentDate(today));
        summary.put("scheduled",
                appointmentRepository.countByStatus(AppointmentStatus.SCHEDULED));
        summary.put("prescriptions", prescriptionRepository.count());

        return summary;
    }
}