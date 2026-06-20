package com.clinic.scms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "prescription_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @NotBlank(message = "Medicine name is required")
    @Column(nullable = false, length = 200)
    private String medicineName;

    @NotBlank(message = "Dosage is required")
    @Column(nullable = false, length = 100)
    private String dosage;

    @NotBlank(message = "Frequency is required")
    @Column(nullable = false, length = 100)
    private String frequency;

    @Min(value = 1, message = "Duration must be at least 1 day")
    @Column(nullable = false)
    private Integer durationDays;
}
