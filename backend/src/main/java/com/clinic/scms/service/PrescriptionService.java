package com.clinic.scms.service;

import com.clinic.scms.entity.Prescription;
import com.clinic.scms.repo.AppointmentRepository;
import com.clinic.scms.repo.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository  appointmentRepository;

    public Prescription createPrescription(Prescription prescription) {
        appointmentRepository.findById(prescription.getAppointment().getAppointmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));
        return prescriptionRepository.save(prescription);
    }

    public Prescription getPrescriptionById(Integer id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prescription not found"));
    }

    public List<Prescription> getPrescriptionsByPatient(Integer patientId) {
        return prescriptionRepository.findByAppointmentPatientPatientId(patientId);
    }

    public Prescription getPrescriptionByAppointment(Integer appointmentId) {
        return prescriptionRepository.findByAppointmentAppointmentId(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prescription not found"));
    }

    public Prescription updatePrescription(Integer id, Prescription updated) {
        Prescription existing = getPrescriptionById(id);
        existing.setNotes(updated.getNotes());
        existing.setItems(updated.getItems());
        return prescriptionRepository.save(existing);
    }

    public void deletePrescription(Integer id) {
        getPrescriptionById(id);
        prescriptionRepository.deleteById(id);
    }
}
