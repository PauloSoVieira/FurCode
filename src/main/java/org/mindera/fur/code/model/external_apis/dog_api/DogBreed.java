package org.mindera.fur.code.model.external_apis.dog_api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Dog breed
 */
@Data
public class DogBreed {

    @NotBlank(message = "ID must be provided")
    @Size(min = 1, max = 255, message = "ID must be between 1 and 255 characters")
    private String id;

    @Valid
    @NotNull(message = "Attributes must be provided")
    private DogBreedAttributes attributes;
}
