package org.mindera.fur.code.dto.external_apis.dog_api;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "DTO containing a list of dog breed names")
public class DogBreedsNamesDTO {

    @Valid
    @Schema(description = "List of dog breed names")
    private List<String> breeds;
}
