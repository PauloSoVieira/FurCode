package org.mindera.fur.code.dto.pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PetBreedDTO {

    @NotEmpty(message = "Breed ID must be provided")
    private Long id;

    @NotBlank(message = "Breed name must be provided")
    private String name;

    @NotBlank(message = "Breed description must be provided")
    private String description;
}
