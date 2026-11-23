package kigali.clinc.clinicmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinc.clinicmanagement.domain.Office;
import kigali.clinc.clinicmanagement.service.OfficeService;
@CrossOrigin
@RestController
@RequestMapping(value = "/api/offices")
public class OfficeController {

   

    @Autowired
    private OfficeService officeService;

    // CREATE
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveOffice(@RequestBody Office office) {

        String returnedMessage = officeService.saveOffice(office);

        if (returnedMessage.equalsIgnoreCase("Office is saved Successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
    }

    // READ ALL
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Office>> getAllOffices() {

        return new ResponseEntity<>(
                officeService.getAllOffices(),
                HttpStatus.OK);
    }

    // READ ONE
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOfficeById(@PathVariable UUID id) {

        Office office = officeService.getOfficeById(id);

        if (office == null) {
            return new ResponseEntity<>(
                    "Office not found",
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(office, HttpStatus.OK);
    }

    // UPDATE
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateOffice(
            @PathVariable UUID id,
            @RequestBody Office office) {

        String returnedMessage = officeService.updateOffice(id, office);

        if (returnedMessage.equalsIgnoreCase(
                "Office is updated Successfully")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.OK);
        }

        if (returnedMessage.equalsIgnoreCase("Office not found")) {
            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                returnedMessage,
                HttpStatus.CONFLICT);
    }

    // get free offices
    @GetMapping("/free")
    public ResponseEntity<List<Office>> findFreeOffices() {
        return ResponseEntity.ok(officeService.findFreeOffices());
    }

    @GetMapping("/busiest")
    public ResponseEntity<?> getBusiestOffice() {

        Object[] result = officeService.getBusiestOffice();

        if (result == null) {
            return ResponseEntity.ok("No appointments yet");
        }

        return ResponseEntity.ok(result);
    }

    // DELETE
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteOffice(@PathVariable UUID id) {

        String returnedMessage = officeService.deleteOffice(id);

        if (returnedMessage.equalsIgnoreCase(
                "Office is deleted Successfully")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(
                returnedMessage,
                HttpStatus.NOT_FOUND);
    }
}