package org.mindera.fur.code.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "breed")
public class Breed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Breed name must be provided")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Animal type must be provided")
    @Column(nullable = false)
    private String animalType;

    @Valid
    @OneToMany(mappedBy = "breed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetType> petTypes; // Changed from @OneToOne(mappedBy = "breedId")
}
