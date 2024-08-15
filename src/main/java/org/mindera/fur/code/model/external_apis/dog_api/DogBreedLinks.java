package org.mindera.fur.code.model.external_apis.dog_api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Links for dog api pagination
 */
@Data
public class DogBreedLinks {

    @NotBlank(message = "Self must be provided")
    @Size(min = 1, max = 255, message = "Self must be between 1 and 255 characters")
    private String self;

    @NotBlank(message = "Related must be provided")
    @Size(min = 1, max = 255, message = "Related must be between 1 and 255 characters")
    private String current;

    @NotBlank(message = "First must be provided")
    @Size(min = 1, max = 255, message = "First must be between 1 and 255 characters")
    private String next;

    @NotBlank(message = "Last must be provided")
    @Size(min = 1, max = 255, message = "Last must be between 1 and 255 characters")
    private String last;
}
