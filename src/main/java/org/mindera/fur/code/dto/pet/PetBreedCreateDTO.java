package org.mindera.fur.code.dto.pet;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.mindera.fur.code.model.enums.pet.PetTypeEnum;

@Data
public class PetBreedCreateDTO {

    @NotBlank(message = "Breed name must be provided")
    private String name;

    @NotNull(message = "Species must be provided")
    @Enumerated(EnumType.STRING)
    private PetTypeEnum type;
}
