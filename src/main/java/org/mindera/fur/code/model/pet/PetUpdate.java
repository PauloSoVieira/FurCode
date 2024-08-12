package org.mindera.fur.code.model.pet;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.mindera.fur.code.model.enums.pet.PetSizeEnum;

@Data
public class PetUpdate {

    private Boolean isAdopted;

    private Boolean isVaccinated;

    @Enumerated(EnumType.STRING)
    private PetSizeEnum size;

    @DecimalMin(value = "0.01", message = "Pet weight must be greater than 0.01 kilos")
    @DecimalMax(value = "999.99", message = "Pet weight must be less than 999.99 kilos")
    private Double weight;

    @Size(min = 3, max = 99, message = "Pet color must be between 3 and 99 characters")
    private String color;

    @Min(value = 1, message = "Pet age must be greater than 1")
    @Max(value = 99, message = "Pet age must be less than 99")
    private Integer age;

    @Size(min = 1, max = 999, message = "Pet observation must be between 1 and 999 characters")
    private String observations;
}
