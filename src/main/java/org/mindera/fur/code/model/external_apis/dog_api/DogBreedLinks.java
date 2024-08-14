package org.mindera.fur.code.model.external_apis.dog_api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Links for dog api pagination
 */
@Data
public class DogBreedLinks {

    /**
     * Self link
     */
    @NotBlank(message = "Self must be provided")
    @Size(min = 1, max = 255, message = "Self must be between 1 and 255 characters")
    private String self;

    /**
     * Related link
     */
    @NotBlank(message = "Related must be provided")
    @Size(min = 1, max = 255, message = "Related must be between 1 and 255 characters")
    private String current;

    /**
     * First link
     */
    @NotBlank(message = "First must be provided")
    @Size(min = 1, max = 255, message = "First must be between 1 and 255 characters")
    private String next;

    /**
     * Last link
     */
    @Size(min = 1, max = 255, message = "Last must be between 1 and 255 characters")
    @NotBlank(message = "Last must be provided")
    private String last;
}
