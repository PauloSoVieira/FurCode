package org.mindera.fur.code.dto.external_apis.dog_api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Dog breed DTO
 */
@Data
@Schema(description = "Dog breed")
public class DogBreedDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Dog breed ID is required")
    @Size(min = 1, max = 255, message = "Dog breed id must be between 1 and 255 characters")
    @Schema(description = "Dog breed external ID")
    private String id;

    @NotBlank(message = "Dog breed name is required")
    @Size(min = 1, max = 255, message = "Dog breed name must be between 1 and 255 characters")
    @Schema(description = "Dog breed name")
    private String name;

    @NotBlank(message = "Dog breed description is required")
    @Size(min = 1, max = 2048, message = "Dog breed description must be between 1 and 2048 characters")
    @Schema(description = "Dog breed description")
    private String description;
}
