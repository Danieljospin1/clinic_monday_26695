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

import kigali.clinc.clinicmanagement.domain.Doctor;
import kigali.clinc.clinicmanagement.service.DoctorService;
import kigali.clinc.clinicmanagement.service.SpecializationService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private SpecializationService specializationService;

    @Autowired
    private DoctorService doctorService;

    // CREATE
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Doctor> saveDoctor(@RequestBody Doctor doctor, @RequestParam int officeNumber) {

        Doctor savedDoctor = doctorService.saveDoctor(doctor, officeNumber);

        return new ResponseEntity<>(
                savedDoctor,
                HttpStatus.CREATED);
    }

    // get doctors by specialization
    @GetMapping("/by-specialization")
    public List<Doctor> getDoctorsBySpecialization(
            @RequestParam String name) {

        return doctorService.getDoctorsBySpecialization(name);
    }

    // get doctors without office
    @GetMapping("/without-office")
    public List<Doctor> getDoctorsWithoutOffice() {
        return doctorService.getDoctorsWithoutOffice();
    }

    // READ ALL
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> getAllDoctors() {

        return new ResponseEntity<>(
                doctorService.getAllDoctors(),
                HttpStatus.OK);
    }

    // READ ONE
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorById(
            @PathVariable UUID id) {

        Doctor doctor = doctorService.getDoctorById(id);

        if (doctor == null) {
            return new ResponseEntity<>(
                    "Doctor not found",
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                doctor,
                HttpStatus.OK);
    }

    // UPDATE
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDoctor(
            @PathVariable UUID id,
            @RequestBody Doctor doctor) {

        String returnedMessage = doctorService.updateDoctor(id, doctor);

        if (returnedMessage.equalsIgnoreCase(
                "Doctor is updated Successfully")) {

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
    public ResponseEntity<?> deleteDoctor(
            @PathVariable UUID id) {

        String returnedMessage = doctorService.deleteDoctor(id);

        if (returnedMessage.equalsIgnoreCase(
                "Doctor is deleted Successfully")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(
                returnedMessage,
                HttpStatus.NOT_FOUND);
    }

    // ADD SPECIALIZATION TO DOCTOR
    @PutMapping("/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<?> addSpecializationToDoctor(
            @PathVariable UUID doctorId,
            @PathVariable UUID specializationId) {

        String message = specializationService.addSpecializationToDoctor(
                specializationId,
                doctorId);

        if (message.equals("That specialization does not exist")
                || message.equals("Doctor with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.equals("The doctor already has that specialization")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(
                message,
                HttpStatus.OK);
    }
}