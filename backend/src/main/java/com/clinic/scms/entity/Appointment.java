package com.clinic.scms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
    name = "appointments",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_doctor_slot",
        columnNames = {"doctor_id", "appointment_date", "appointment_time"}
    )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appointmentId;

    @NotNull(message = "Patient is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull(message = "Doctor is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment date cannot be in the past")
    @Column(nullable = false)
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    @Column(nullable = false)
    private LocalTime appointmentTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.SCHEDULED;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public enum Status { SCHEDULED, COMPLETED, CANCELLED, NO_SHOW }
}
