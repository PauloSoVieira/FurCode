package org.mindera.fur.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "medical_record")
public class PetRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Pet is required")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet; // Change from Pet to Long???? Change from petId to pet???

    @NotNull(message = "Vaccination status is required")
    @Column(nullable = false)
    private boolean isVaccinated;

    // Eliminar os dois booleans abaixo e criar um enum de intervenções
    //@NotNull(message = "Intervention type is required")
    //private MedicalIntervention interventionType;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    @Column(nullable = false)
    private Date date;

    @Size(max = 999, message = "Observation cannot be longer than 999 characters")
    @Column(length = 999)
    private String observation;
}
