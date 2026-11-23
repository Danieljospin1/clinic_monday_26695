package kigali.clinc.clinicmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinc.clinicmanagement.domain.Doctor;
import kigali.clinc.clinicmanagement.domain.Office;
import kigali.clinc.clinicmanagement.repository.DoctorRepository;
import kigali.clinc.clinicmanagement.repository.OfficeRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;
    @Autowired
    private OfficeRepository officeRep;

    // CREATE
    public Doctor saveDoctor(Doctor doctor, int officeNumber) {
        //// Optional<Office> getOffice=officeRep.findByOfficeNumber(officeNumber);

        return doctorRepo.save(doctor);
    }

    // READ ALL
    public List<Doctor> getAllDoctors() {

        return doctorRepo.findAll();
    }

    // READ ONE
    public Doctor getDoctorById(UUID id) {

        return doctorRepo.findById(id).orElse(null);
    }

    public List<Doctor> searchDoctorsByName(String name) {
        return doctorRepo.searchDoctorsByName(name);
    }

    public List<Doctor> getDoctorsBySpecialization(String name) {
        return doctorRepo.findDoctorsBySpecialization(name);
    }

    public List<Doctor> getDoctorsWithoutOffice() {
        return doctorRepo.findDoctorsWithoutOffice();
    }

    // UPDATE
    public String updateDoctor(UUID id, Doctor updatedDoctor) {

        Doctor existingDoctor = doctorRepo.findById(id).orElse(null);

        if (existingDoctor == null) {
            return "Doctor not found";
        }

        existingDoctor.setFirstName(
                updatedDoctor.getFirstName());

        existingDoctor.setLastName(
                updatedDoctor.getLastName());

        existingDoctor.setDateOfBirth(
                updatedDoctor.getDateOfBirth());

        doctorRepo.save(existingDoctor);

        return "Doctor is updated Successfully";
    }

    // DELETE
    public String deleteDoctor(UUID id) {

        Doctor doctor = doctorRepo.findById(id).orElse(null);

        if (doctor == null) {
            return "Doctor not found";
        }

        doctorRepo.delete(doctor);

        return "Doctor is deleted Successfully";
    }

    @Transactional
    public String changeDoctorOffice(UUID doctorId, int officeNumber) {

        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);

        if (doctor == null) {
            return "Doctor with that id does not exist";
        }

        Office office = officeRep.findByOfficeNumber(officeNumber).orElse(null);

        if (office == null) {
            return "Office with that number does not exist";
        }

        if (office.getDoctor() != null) {
            return "That office is already assigned to another doctor";
        }

        doctor.setOffice(office);

        doctorRepo.save(doctor);

        return "Office changed successfully";
    }
}