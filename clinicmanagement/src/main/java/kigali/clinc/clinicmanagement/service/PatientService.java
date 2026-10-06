package kigali.clinc.clinicmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.Patient;
import kigali.clinc.clinicmanagement.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;

    // CREATE
    public Patient savePatient(Patient patient) {

        return patientRepo.save(patient);
    }

    // READ ALL
    public List<Patient> getAllPatients() {

        return patientRepo.findAll();
    }

    public List<Patient> getPatientsByLastName(String lastName) {
        return patientRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);

    };

    // READ ONE
    public Patient getPatientById(UUID id) {

        return patientRepo.findById(id).orElse(null);
    };

    // UPDATE
    public String updatePatient(UUID id, Patient updatedPatient) {

        Patient existingPatient = patientRepo.findById(id).orElse(null);

        if (existingPatient == null) {
            return "Patient not found";
        }

        existingPatient.setFirstName(updatedPatient.getFirstName());
        existingPatient.setLastName(updatedPatient.getLastName());
        existingPatient.setDateOfBirth(updatedPatient.getDateOfBirth());

        patientRepo.save(existingPatient);

        return "Patient is updated Successfully";
    }

    // DELETE
    public String deletePatient(UUID id) {

        Patient patient = patientRepo.findById(id).orElse(null);

        if (patient == null) {
            return "Patient not found";
        }

        patientRepo.delete(patient);

        return "Patient is deleted Successfully";
    }
}