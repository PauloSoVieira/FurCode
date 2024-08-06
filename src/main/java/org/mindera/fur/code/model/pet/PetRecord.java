package org.mindera.fur.code.model.pet;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "pet_record")
public class PetRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Pet is required")
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @NotNull(message = "Vaccination status is required")
    @Column(nullable = false)
    private Boolean isVaccinated;

    // Change to enum that have a multiple values of the interventions and a description
    @NotBlank(message = "Intervention type is required")
    // @NotNull(message = "Intervention type is required")
    private String petRecordsStatus;

    @Valid
    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    @Column(nullable = false)
    private Date date;

    @NotBlank(message = "Observation is required")
    @Size(max = 999, message = "Observation cannot be longer than 999 characters")
    @Column(length = 999)
    private String observation;
}
