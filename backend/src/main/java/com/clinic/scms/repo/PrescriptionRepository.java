package com.clinic.scms.repo;

import com.clinic.scms.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {

    Optional<Prescription> findByAppointmentAppointmentId(Integer appointmentId);

    List<Prescription> findByAppointmentPatientPatientId(Integer patientId);
}
