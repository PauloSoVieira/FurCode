package org.mindera.fur.code.dto.external_apis.dog_api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Dog breeds names DTO
 */
@Data
@AllArgsConstructor
@Schema(description = "Dog breeds names")
public class DogBreedsNamesDTO implements Serializable {
    /**
     * Serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Dog breeds names
     */
    @Valid
    @NotNull(message = "Breeds must be provided")
    @Schema(description = "List of dog breeds names")
    private List<String> breeds;
}
