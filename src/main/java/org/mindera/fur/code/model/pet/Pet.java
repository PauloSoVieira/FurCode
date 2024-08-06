package org.mindera.fur.code.model.pet;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.mindera.fur.code.model.Shelter;

import java.util.List;

@Data
@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Pet name must be provided")
    @Size(min = 1, max = 30, message = "Pet name must be between 1 and 30 characters")
    @Column(nullable = false)
    private String name;

    @Valid
    @NotNull(message = "Pet Type must be provided")
    @OneToOne
    @JoinColumn(name = "pet_type_id", nullable = false)
    private PetType petType; // changed to int-petTypeId from Pet-petType

    @Valid
    @NotNull(message = "Shelter must be provided")
    @ManyToOne
    @JoinColumn(name = "shelter_id", nullable = false)
    private Shelter shelter;

    @NotNull(message = "Adopted status must be provided")
    @Column(nullable = false)
    private Boolean isAdopted;

    //@NotNull(message = "Size must be provided")
    @NotBlank(message = "Size must be provided")
    //@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String size;

    // TODO: add isVacinated

    @NotNull(message = "Pet weight must be provided")
    @DecimalMin(value = "0.01", message = "Pet weight must be greater than 0.01 kilos")
    @DecimalMax(value = "999.99", message = "Pet weight must be less than 999.99 kilos")
    @Column(nullable = false)
    private Double weight;

    @NotBlank(message = "Pet color must be provided")
    @Size(min = 3, max = 99, message = "Pet color must be between 3 and 99 characters")
    @Column(nullable = false)
    private String color;

    @NotNull(message = "Pet age must be provided")
    @Min(value = 1, message = "Pet age must be greater than 1")
    @Max(value = 99, message = "Pet age must be less than 99")
    @Column(nullable = false)
    private Integer age;

    @NotBlank(message = "Pet observation must be provided")
    @Size(min = 1, max = 999, message = "Pet observation must be between 1 and 999 characters")
    @Column(nullable = false)
    private String observations;

    @Valid
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetRecord> petRecords;
}
