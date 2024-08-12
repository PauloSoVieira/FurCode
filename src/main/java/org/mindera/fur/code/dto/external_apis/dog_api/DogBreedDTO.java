package org.mindera.fur.code.dto.external_apis.dog_api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DogBreedDTO {

    @NotBlank(message = "Dog breed ID is required")
    @Size(min = 1, max = 255, message = "Dog breed id must be between 1 and 255 characters")
    private String id;

    @NotBlank(message = "Dog breed name is required")
    @Size(min = 1, max = 255, message = "Dog breed name must be between 1 and 255 characters")
    private String name;

    @NotBlank(message = "Dog breed description is required")
    @Size(min = 1, max = 2048, message = "Dog breed description must be between 1 and 2048 characters")
    private String description;
}
