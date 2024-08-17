package org.mindera.fur.code.dto.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * A DTO class for a pet breed.
 */
@Data
public class PetBreedDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Positive(message = "Breed ID must be greater than 0")
    @NotNull(message = "Breed ID must be provided")
    @Schema(description = "The breed ID", example = "3")
    private Long id;

    @NotBlank(message = "External API ID must be provided")
    @Size(min = 1, max = 255, message = "External API ID must be between 1 and 255 characters")
    @Schema(description = "The external API ID of the pet breed", example = "23yrt-tht-r46")
    private String externalApiId;

    @NotBlank(message = "Breed name must be provided")
    @Size(min = 1, max = 255, message = "Pet breed name must be between 1 and 255 characters")
    @Schema(description = "The name of the pet breed", example = "São Bernardo")
    private String name;

    @NotBlank(message = "Breed description must be provided")
    @Size(min = 1, max = 2048, message = "Pet breed description must be between 1 and 2048 characters")
    @Schema(description = "The description of the pet breed", example = "A large and friendly dog breed")
    private String description;
}
