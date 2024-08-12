package org.mindera.fur.code.model.external_apis.dog_api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class DogBreedResponse {

    @Valid
    @NotEmpty(message = "Data must be provided")
    private List<DogBreed> data;

    @Valid
    @NotEmpty(message = "Links must be provided")
    private DogBreedLinks links;
}
