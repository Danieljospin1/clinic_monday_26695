package kigali.clinc.clinicmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinc.clinicmanagement.domain.Specialization;
import kigali.clinc.clinicmanagement.service.SpecializationService;

@RestController
@RequestMapping("/api/specialization")
public class SpecializationController {

        @Autowired
        private SpecializationService specializationService;

        // CREATE
        @PostMapping("/save")
        public ResponseEntity<?> saveSpecialization(
                        @RequestBody Specialization specialization) {

                String message = specializationService.saveSpecialization(specialization);

                if (message.equalsIgnoreCase(
                                "Specialization saved successfully")) {

                        return new ResponseEntity<>(
                                        message,
                                        HttpStatus.CREATED);
                }

                return new ResponseEntity<>(
                                message,
                                HttpStatus.CONFLICT);
        }

        // READ ALL
        @GetMapping("/all")
        public ResponseEntity<List<Specialization>> getAllSpecializations() {

                return new ResponseEntity<>(
                                specializationService.getAllSpecializations(),
                                HttpStatus.OK);
        }

        // READ ONE
        @GetMapping("/{id}")
        public ResponseEntity<?> getSpecializationById(
                        @PathVariable UUID id) {

                Specialization specialization = specializationService
                                .getSpecializationById(id);

                if (specialization == null) {

                        return new ResponseEntity<>(
                                        "Specialization not found",
                                        HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<>(
                                specialization,
                                HttpStatus.OK);
        }

        // UPDATE
        @PutMapping("/{id}")
        public ResponseEntity<?> updateSpecialization(
                        @PathVariable UUID id,
                        @RequestBody Specialization specialization) {

                String message = specializationService.updateSpecialization(
                                id,
                                specialization);

                if (message.equalsIgnoreCase(
                                "Specialization updated successfully")) {

                        return new ResponseEntity<>(
                                        message,
                                        HttpStatus.OK);
                }

                return new ResponseEntity<>(
                                message,
                                HttpStatus.NOT_FOUND);
        }

        @GetMapping("/unused")
        public List<Specialization> getUnusedSpecializations() {
                return specializationService.getUnusedSpecializations();
        }

        // DELETE
        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteSpecialization(
                        @PathVariable UUID id) {

                String message = specializationService.deleteSpecialization(id);

                if (message.equalsIgnoreCase(
                                "Specialization deleted successfully")) {

                        return new ResponseEntity<>(
                                        message,
                                        HttpStatus.OK);
                }

                return new ResponseEntity<>(
                                message,
                                HttpStatus.NOT_FOUND);
        }

        // ADD DOCTOR TO SPECIALIZATION
        @PostMapping("/{specializationId}/doctor/{doctorId}")
        public ResponseEntity<?> addDoctorToSpecialization(
                        @PathVariable UUID specializationId,
                        @PathVariable UUID doctorId) {

                String message = specializationService.addDoctorToSpecialization(
                                specializationId,
                                doctorId);

                return new ResponseEntity<>(
                                message,
                                HttpStatus.OK);
        }
}