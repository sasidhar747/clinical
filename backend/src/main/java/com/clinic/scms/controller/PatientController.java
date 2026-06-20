package com.clinic.scms.controller;

import com.clinic.scms.entity.Patient;
import com.clinic.scms.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(patientService.createPatient(patient));
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Integer id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatients(@RequestParam String name) {
        return ResponseEntity.ok(patientService.searchByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Integer id,
            @Valid @RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.updatePatient(id, patient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
