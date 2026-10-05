package kigali.clinc.clinicmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.Doctor;
import kigali.clinc.clinicmanagement.domain.Specialization;
import kigali.clinc.clinicmanagement.repository.DoctorRepository;
import kigali.clinc.clinicmanagement.repository.SpecializationRepository;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepo;

    @Autowired
    private DoctorRepository doctorRepo;


    // CREATE
    public String saveSpecialization(Specialization specialization) {

        if (specializationRepo.existsByName(specialization.getName())) {
            return "Specialization already exists";
        }

        specializationRepo.save(specialization);

        return "Specialization saved successfully";
    }


    // READ ALL
    public List<Specialization> getAllSpecializations() {

        return specializationRepo.findAll();
    }


    // READ ONE
    public Specialization getSpecializationById(UUID id) {

        return specializationRepo.findById(id).orElse(null);
    }


    // UPDATE
    public String updateSpecialization(
            UUID id,
            Specialization newData) {

        Specialization existing =
                specializationRepo.findById(id).orElse(null);

        if (existing == null) {
            return "Specialization not found";
        }

        existing.setName(newData.getName());

        specializationRepo.save(existing);

        return "Specialization updated successfully";
    }


    // DELETE
    public String deleteSpecialization(UUID id) {

        if (!specializationRepo.existsById(id)) {
            return "Specialization not found";
        }

        specializationRepo.deleteById(id);

        return "Specialization deleted successfully";
    }


    // ADD DOCTOR TO SPECIALIZATION
    public String addDoctorToSpecialization(
            UUID specializationId,
            UUID doctorId) {

        Specialization specialization =
                specializationRepo.findById(specializationId)
                .orElse(null);

        if (specialization == null) {
            return "Specialization not found";
        }

        Doctor doctor =
                doctorRepo.findById(doctorId)
                .orElse(null);

        if (doctor == null) {
            return "Doctor not found";
        }

        if (specialization.getDoctors().contains(doctor)) {
            return "Doctor already has this specialization";
        }

        specialization.getDoctors().add(doctor);

        specializationRepo.save(specialization);

        return "Doctor added to specialization successfully";
    }
}