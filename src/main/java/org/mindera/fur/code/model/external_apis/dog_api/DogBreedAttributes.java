package org.mindera.fur.code.model.external_apis.dog_api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Dog breed attributes
 */
@Data
public class DogBreedAttributes {

    @NotBlank(message = "Name must be provided")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @NotBlank(message = "Description must be provided")
    @Size(min = 1, max = 2048, message = "Description must be between 1 and 2048 characters")
    private String description;
}
