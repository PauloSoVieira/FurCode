package org.mindera.fur.code.model.pet;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.mindera.fur.code.model.enums.pet.PetSpeciesEnum;

@Data
@Entity
@Table(name = "pet_type")
public class PetType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Valid
    @NotNull(message = "Pet species name must be provided")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetSpeciesEnum species;

    @Valid
    @NotNull
    @ManyToOne
    @JoinColumn(name = "pet_breed_id", nullable = false)
    private PetBreed breed;
}
