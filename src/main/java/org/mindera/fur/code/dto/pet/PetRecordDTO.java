package org.mindera.fur.code.dto.pet;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
public class PetRecordDTO {

    @NotEmpty(message = "Pet ID is required")
    private Long id;

    @NotEmpty(message = "Pet ID is required")
    private Long petId;

    // Criar um enum de intervenções
    @NotBlank(message = "Intervention type is required")
    // @NotNull(message = "Intervention type is required")
    private String petRecordsStatus;

    @NotNull(message = "Observation is required")
    @Size(max = 999, message = "Observation cannot be longer than 999 characters")
    private String observation;

    @Valid
    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private Date date;
}
