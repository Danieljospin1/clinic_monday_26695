package kigali.clinc.clinicmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public List<Specialization> getUnusedSpecializations() {
        return specializationRepo.findUnusedSpecializations();
    }

    // UPDATE
    public String updateSpecialization(
            UUID id,
            Specialization newData) {

        Specialization existing = specializationRepo.findById(id).orElse(null);

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

    // ADD specialization to a doctor
    public String addSpecializationToDoctor(
            UUID specializationId,
            UUID doctorId) {

        Specialization specialization = specializationRepo.findById(specializationId)
                .orElse(null);

        if (specialization == null) {
            return "That specialization does not exist";
        }

        Doctor doctor = doctorRepo.findById(doctorId)
                .orElse(null);

        if (doctor == null) {
            return "Doctor with that id does not exist";
        }

        if (specialization.getDoctors().contains(doctor)) {
            return "The doctor already has that specialization";
        }

        specialization.getDoctors().add(doctor);

        specializationRepo.save(specialization);

        return "Doctor added to specialization successfully";
    };

    // Removing specialization from doctor
@Transactional
public String removeSpecializationFromDoctor(
        UUID specializationId,
        UUID doctorId) {

    Specialization specialization = specializationRepo
            .findById(specializationId)
            .orElse(null);

    if (specialization == null) {
        return "That specialization does not exist";
    }

    Doctor doctor = doctorRepo.findById(doctorId)
            .orElse(null);

    if (doctor == null) {
        return "Doctor with that id does not exist";
    }

    boolean removed = doctor.getSpecializations()
            .removeIf(s -> s.getId().equals(specializationId));

    if (removed) {
        doctorRepo.save(doctor);

        return "Specialization removed from doctor successfully";
    }

    return "Doctor does not have that specialization";
}
}