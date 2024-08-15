package org.mindera.fur.code.model.external_apis.dog_api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Response for dog api by id
 */
@Data
public class DogBreedByIdResponse {

    @Valid
    @NotNull(message = "Data must be provided")
    private DogBreed data;
}
