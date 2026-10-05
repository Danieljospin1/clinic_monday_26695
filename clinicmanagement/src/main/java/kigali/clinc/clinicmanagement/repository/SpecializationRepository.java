package kigali.clinc.clinicmanagement.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.clinc.clinicmanagement.domain.Specialization;

@Repository
public interface SpecializationRepository
        extends JpaRepository<Specialization, UUID> {

    Optional<Specialization> findByName(String name);

    Boolean existsByName(String name);
}