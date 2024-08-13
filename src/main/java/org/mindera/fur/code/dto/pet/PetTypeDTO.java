package org.mindera.fur.code.dto.pet;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.mindera.fur.code.model.enums.pet.PetSpeciesEnum;

@Data
public class PetTypeDTO {

    @NotEmpty(message = "Pet specie ID must be provided")
    private Long id;

    @NotEmpty(message = "Pet breed ID must be provided")
    private Long breedId;

    @NotNull(message = "Pet specie name must be provided")
    @Enumerated(EnumType.STRING)
    private PetSpeciesEnum species;
}
