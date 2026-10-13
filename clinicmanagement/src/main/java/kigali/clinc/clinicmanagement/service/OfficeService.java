package kigali.clinc.clinicmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.Office;
import kigali.clinc.clinicmanagement.repository.OfficeRepository;

@Service
public class OfficeService {

    @Autowired
    private OfficeRepository officeRepo;

    // CREATE
    public String saveOffice(Office office) {

        if (officeRepo.findByOfficeNumber(office.getOfficeNumber()) != null) {
            return "Office with that number "
                    + office.getOfficeNumber()
                    + " already exists";
        }

        officeRepo.save(office);

        return "Office is saved Successfully";
    }

    // READ ALL
    public List<Office> getAllOffices() {
        return officeRepo.findAll();
    }

    // READ ONE
    public Office getOfficeById(UUID id) {
        return officeRepo.findById(id).orElse(null);
    }

    public Object[] getBusiestOffice() {

        List<Object[]> results = officeRepo.findBusiestOffice();

        if (results.isEmpty()) {
            return null;
        }

        return results.get(0);
    }
    // free offices

    public List<Office> findFreeOffices() {
        return officeRepo.findFreeOffices();
    }

    // UPDATE
    public String updateOffice(UUID id, Office updatedOffice) {

        Office existingOffice = officeRepo.findById(id).orElse(null);

        if (existingOffice == null) {
            return "Office not found";
        }

        // Check whether the new office number belongs to another office
        Office officeWithSameNumber = officeRepo.findByOfficeNumber(updatedOffice.getOfficeNumber());

        if (officeWithSameNumber != null
                && !officeWithSameNumber.getId().equals(id)) {
            return "Office with that number "
                    + updatedOffice.getOfficeNumber()
                    + " already exists";
        }

        existingOffice.setName(updatedOffice.getName());
        existingOffice.setOfficeNumber(updatedOffice.getOfficeNumber());

        officeRepo.save(existingOffice);

        return "Office is updated Successfully";
    }

    // DELETE
    public String deleteOffice(UUID id) {

        Office office = officeRepo.findById(id).orElse(null);

        if (office == null) {
            return "Office not found";
        }

        officeRepo.delete(office);

        return "Office is deleted Successfully";
    }
}