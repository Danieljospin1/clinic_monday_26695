package kigali.clinc.clinicmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinc.clinicmanagement.domain.Patient;
import kigali.clinc.clinicmanagement.service.PatientService;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

        @Autowired
        private PatientService patientService;

        // CREATE
        @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<Patient> savePatient(
                        @RequestBody Patient patient) {

                Patient savedPatient = patientService.savePatient(patient);

                return new ResponseEntity<>(
                                savedPatient,
                                HttpStatus.CREATED);
        }

        // READ ALL
        @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<List<Patient>> getAllPatients() {

                return new ResponseEntity<>(
                                patientService.getAllPatients(),
                                HttpStatus.OK);
        }

        // READ ONE
        @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> getPatientById(
                        @PathVariable UUID id) {

                Patient patient = patientService.getPatientById(id);

                if (patient == null) {
                        return new ResponseEntity<>(
                                        "Patient not found",
                                        HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<>(
                                patient,
                                HttpStatus.OK);
        }

        @GetMapping(value = "by-last-name", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<Patient> getPatientByLastName(@RequestParam String lastName) {
                List<Patient> patientsLastName = patientService.getPatientsByName(lastName);
                return new ResponseEntity<>(
                                patientsLastName,
                                HttpStatus.OK);
        }

        // UPDATE
        @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> updatePatient(
                        @PathVariable UUID id,
                        @RequestBody Patient patient) {

                String returnedMessage = patientService.updatePatient(id, patient);

                if (returnedMessage.equalsIgnoreCase(
                                "Patient is updated Successfully")) {

                        return new ResponseEntity<>(
                                        returnedMessage,
                                        HttpStatus.OK);
                }

                return new ResponseEntity<>(
                                returnedMessage,
                                HttpStatus.NOT_FOUND);
        }

        // DELETE
        @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> deletePatient(
                        @PathVariable UUID id) {

                String returnedMessage = patientService.deletePatient(id);

                if (returnedMessage.equalsIgnoreCase(
                                "Patient is deleted Successfully")) {

                        return new ResponseEntity<>(
                                        returnedMessage,
                                        HttpStatus.OK);
                }

                return new ResponseEntity<>(
                                returnedMessage,
                                HttpStatus.NOT_FOUND);
        }
}