package kigali.clinc.clinicmanagement.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinc.clinicmanagement.domain.Prescription;
import kigali.clinc.clinicmanagement.service.PrescriptionService;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> savePrescription(
            @RequestParam UUID appointmentId,
            @RequestBody Prescription prescription) {

        String result = prescriptionService.savePrescription(
                prescription, appointmentId);

        if (result.equals("The appointment with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        if (result.equals("Prescriptions are only for COMPLETED appointments")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    
}