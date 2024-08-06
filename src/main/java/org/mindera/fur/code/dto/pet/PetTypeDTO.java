package org.mindera.fur.code.dto.pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PetTypeDTO {

    private Long id;

    @NotBlank(message = "Pet type name must be provided")
    private String name;

    @NotNull(message = "Breed ID must be provided")
    private Long breedId;
}
