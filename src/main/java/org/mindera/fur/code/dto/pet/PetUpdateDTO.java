package org.mindera.fur.code.dto.pet;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.mindera.fur.code.model.enums.pet.PetSizeEnum;

import java.time.LocalDate;

/**
 * A DTO class for updating a pet.
 */
@Data
public class PetUpdateDTO {

    @Schema(description = "The adopted status of the pet", example = "true")
    private Boolean isAdopted;

    @Schema(description = "The vaccination status of the pet", example = "true")
    private Boolean isVaccinated;

    @Enumerated(EnumType.STRING)
    @Schema(description = "The size of the pet", example = "SMALL")
    private PetSizeEnum size;

    @DecimalMin(value = "0.01", message = "Pet weight must be greater than 0.01 kilos")
    @DecimalMax(value = "999.99", message = "Pet weight must be less than 999.99 kilos")
    @Schema(description = "The weight of the pet", example = "9.9")
    private Double weight;

    @Size(min = 3, max = 99, message = "Pet color must be between 3 and 99 characters")
    @Schema(description = "The color of the pet", example = "Blue")
    private String color;

    @NotNull(message = "Pet age must be provided")
    @PastOrPresent(message = "Pet birth date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "The age of the pet", example = "2023-01-01")
    private LocalDate dateOfBirth;

    @Size(min = 1, max = 999, message = "Pet observation must be between 1 and 999 characters")
    @Schema(description = "The observations of the pet", example = "Healthy")
    private String observations;
}
