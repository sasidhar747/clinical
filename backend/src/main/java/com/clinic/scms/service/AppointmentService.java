package com.clinic.scms.service;

import com.clinic.scms.entity.Appointment;
import com.clinic.scms.entity.Patient;
import com.clinic.scms.repo.AppointmentRepository;
import com.clinic.scms.repo.DoctorRepository;
import com.clinic.scms.repo.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository     patientRepository;
    private final DoctorRepository      doctorRepository;

    public Appointment bookAppointment(Appointment appointment) {
        // Conflict check — doctor double-booking
        boolean slotTaken = appointmentRepository.existsByDoctorDoctorIdAndAppointmentDateAndAppointmentTime(
                appointment.getDoctor().getDoctorId(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime());
        if (slotTaken) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Doctor already has an appointment at this date and time");
        }
        appointment.setStatus(Appointment.Status.SCHEDULED);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointments(LocalDate date, Integer doctorId, Integer patientId) {
        if (doctorId != null)  return appointmentRepository.findByDoctorDoctorId(doctorId);
        if (patientId != null) return appointmentRepository.findByPatientPatientId(patientId);
        if (date != null)      return appointmentRepository.findByAppointmentDate(date);
        return appointmentRepository.findAll();
    }

    public Appointment getById(Integer id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));
    }

    public Appointment updateStatus(Integer id, Appointment.Status status) {
        Appointment appt = getById(id);
        appt.setStatus(status);
        return appointmentRepository.save(appt);
    }

    public void cancelAppointment(Integer id) {
        Appointment appt = getById(id);
        appt.setStatus(Appointment.Status.CANCELLED);
        appointmentRepository.save(appt);
    }

    public List<Appointment> getAppointmentsByPatient(Integer patientId) {
        return appointmentRepository.findByPatientPatientId(patientId);
    }
}
