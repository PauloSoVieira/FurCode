package org.mindera.fur.code.dto.pet;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.mindera.fur.code.model.enums.pet.PetSpeciesEnum;

@Data
public class PetTypeDTO {

    @NotEmpty(message = "Pet specie ID must be provided")
    private Long id;

    @NotNull(message = "Breed ID must be provided")
    private Long breedId;

    @NotNull(message = "Pet species name must be provided")
    @Enumerated(EnumType.STRING)
    private PetSpeciesEnum species;
}
