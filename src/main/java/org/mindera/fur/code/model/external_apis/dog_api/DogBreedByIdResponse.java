package org.mindera.fur.code.model.external_apis.dog_api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DogBreedByIdResponse {

    @Valid
    @NotEmpty(message = "Data must be provided")
    private DogBreed data;
}
