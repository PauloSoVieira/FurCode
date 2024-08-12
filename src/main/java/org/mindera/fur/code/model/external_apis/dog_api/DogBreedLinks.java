package org.mindera.fur.code.model.external_apis.dog_api;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DogBreedLinks {

    @NotBlank(message = "Self must be provided")
    private String self;

    @NotBlank(message = "Related must be provided")
    private String current;

    @NotBlank(message = "First must be provided")
    private String next;

    @NotBlank(message = "Last must be provided")
    private String last;
}
