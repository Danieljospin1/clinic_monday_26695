package kigali.clinc.clinicmanagement.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // POST /appointments
    @PostMapping("/save")
    public ResponseEntity<String> saveAppointment(
            @RequestBody Appointment appointment) {

        String message = appointmentService.saveAppointment(appointment);

        if (message.equals("Appointment date cannot be in the past")) {
            return ResponseEntity.badRequest().body(message);
        }
        ;
        if (message.equals("Doctor is already booked on that date")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
        ;

        return ResponseEntity.ok(message);
    }

    // GET /appointments
    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/stats/by-status")
    public List<Object[]> countAppointmentsByStatus() {
        return appointmentService.countAppointmentsByStatus();
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelAppointmentsForDoctorOnDate(
            @RequestParam UUID doctorId,
            @RequestParam String date) {

        Date appointmentDate = Date.valueOf(date);

        String result = appointmentService.cancelAppointmentsForDoctorOnDate(
                doctorId,
                appointmentDate);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-status")
    public List<Appointment> getAppointmentsByStatus(
            @RequestParam AppointmentStatus status) {
        return appointmentService.getAppointmentsByStatus(status);
    }

    // GET /appointments/{id}
    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable UUID id) {
        return appointmentService.getAppointmentById(id);
    }

    // GET /appointments/doctor/{doctorId}
    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getAppointmentsByDoctor(
            @PathVariable UUID doctorId) {

        return appointmentService.getAppointmentsByDoctor(doctorId);
    }

    // GET /appointments/doctor/{doctorId}/count
    @GetMapping("/doctor/{doctorId}/count")
    public long getAppointmentCountByDoctor(
            @PathVariable UUID doctorId) {

        return appointmentService.getAppointmentCountByDoctor(doctorId);
    }

    @GetMapping("/between")
    public List<Appointment> getAppointmentsBetweenDates(
            @RequestParam String start,
            @RequestParam String end) {

        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        return appointmentService.getAppointmentsBetweenDates(
                sqlStartDate,
                sqlEndDate);
    }

    // PUT /appointments/{id}
    @PutMapping("/{id}")
    public Appointment updateAppointment(
            @PathVariable UUID id,
            @RequestBody Appointment appointment) {

        return appointmentService.updateAppointment(id, appointment);
    }

    // DELETE /appointments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable UUID id) {

        appointmentService.deleteAppointment(id);

        return ResponseEntity.ok("Appointment deleted successfully");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateAppointmentStatus(
            @PathVariable UUID id,
            @RequestParam AppointmentStatus status) {

        String result = appointmentService.updateAppointmentStatus(id, status);

        if (result.equals("The appointment with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        if (result.startsWith("Cannot change status from ")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }
}