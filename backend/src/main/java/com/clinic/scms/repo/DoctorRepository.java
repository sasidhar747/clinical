package com.clinic.scms.repo;

import com.clinic.scms.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    boolean existsByEmail(String email);

    List<Doctor> findByIsActiveTrue();

    List<Doctor> findBySpecialisationContainingIgnoreCaseAndIsActiveTrue(String specialisation);

    List<Doctor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);
}
