package com.clinic.scms.repo;

import com.clinic.scms.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findByAppointmentDate(LocalDate date);

    List<Appointment> findByDoctorDoctorId(Integer doctorId);

    List<Appointment> findByPatientPatientId(Integer patientId);

    boolean existsByDoctorDoctorIdAndAppointmentDateAndAppointmentTime(
            Integer doctorId, LocalDate date, LocalTime time);
}
