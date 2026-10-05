package kigali.clinc.clinicmanagement.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.clinc.clinicmanagement.domain.Office;



@Repository 
public interface OfficeRepository extends  JpaRepository<Office,UUID >{
    
    Office findByOfficeNumber(int officeNumber);


    Optional<Office> findByNameAndOfficeNumber(String name, int officeNumber);
 

    Boolean existsByOfficeNumber(int officeNumber);
}
