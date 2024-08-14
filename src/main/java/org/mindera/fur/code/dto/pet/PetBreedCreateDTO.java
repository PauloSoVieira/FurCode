package org.mindera.fur.code.dto.pet;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.mindera.fur.code.model.enums.pet.PetSpeciesEnum;

@Data
public class PetBreedCreateDTO {

    @NotBlank(message = "Pet breed name must be provided")
    @Size(min = 1, max = 255, message = "Pet breed name must be between 1 and 255 characters")
    private String name;

    @NotNull(message = "Pet species must be provided")
    @Enumerated(EnumType.STRING)
    private PetSpeciesEnum species;
}
