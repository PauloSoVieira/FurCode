package org.mindera.fur.code.dto.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.mindera.fur.code.model.enums.pet.PetSpeciesEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * A DTO class for creating a pet breed.
 */
@Data
public class PetBreedCreateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Pet breed name must be provided")
    @Size(min = 1, max = 255, message = "Pet breed name must be between 1 and 255 characters")
    @Schema(description = "The name of the pet breed", example = "Hokkaido")
    private String name;

    @NotNull(message = "Pet species must be provided")
    @Enumerated(EnumType.STRING)
    @Schema(description = "The species of the pet breed", example = "DOG")
    private PetSpeciesEnum species;
}
