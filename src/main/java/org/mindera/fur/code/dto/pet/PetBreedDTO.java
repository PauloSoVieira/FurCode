package org.mindera.fur.code.dto.pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PetBreedDTO {

    @NotEmpty(message = "Pet breed ID must be provided")
    private Long id;

    @NotBlank(message = "External API ID must be provided")
    @Size(min = 1, max = 255, message = "External API ID must be between 1 and 255 characters")
    private String externalApiId;

    @NotBlank(message = "Pet breed name must be provided")
    @Size(min = 1, max = 255, message = "Pet breed name must be between 1 and 255 characters")
    private String name;

    @NotBlank(message = "Pet breed description must be provided")
    @Size(min = 1, max = 2048, message = "Pet breed description must be between 1 and 2048 characters")
    private String description;
}
