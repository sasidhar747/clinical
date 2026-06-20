package com.clinic.scms.service;

import com.clinic.scms.entity.Doctor;
import com.clinic.scms.repo.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public Doctor addDoctor(Doctor doctor) {
        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findByIsActiveTrue();
    }

    public Doctor getDoctorById(Integer id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));
    }

    public List<Doctor> getDoctorsBySpeciality(String speciality, String time) {
        return doctorRepository.findBySpecialisationContainingIgnoreCaseAndIsActiveTrue(speciality);
    }

    public List<Doctor> searchByName(String name) {
        return doctorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }

    public Doctor updateDoctor(Integer id, Doctor updated) {
        Doctor existing = getDoctorById(id);
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setSpecialisation(updated.getSpecialisation());
        existing.setPhone(updated.getPhone());
        existing.setIsActive(updated.getIsActive());
        return doctorRepository.save(existing);
    }

    public void deleteDoctor(Integer id) {
        Doctor doctor = getDoctorById(id);
        doctor.setIsActive(false);
        doctorRepository.save(doctor);
    }
}
