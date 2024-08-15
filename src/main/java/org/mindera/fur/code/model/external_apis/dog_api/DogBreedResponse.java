package org.mindera.fur.code.model.external_apis.dog_api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Response for dog api
 */
@Data
public class DogBreedResponse {

    @Valid
    @NotNull(message = "Data must be provided")
    private List<DogBreed> data;

    @Valid
    @NotNull(message = "Links must be provided")
    private DogBreedLinks links;
}
