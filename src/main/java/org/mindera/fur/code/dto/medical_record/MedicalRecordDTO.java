package org.mindera.fur.code.dto.medical_record;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class MedicalRecordDTO {

    private Long id;

    @NotNull(message = "Pet ID is required")
    private Long petId;

    @NotNull(message = "Vaccination status is required")
    private boolean isVaccinated;

    // Eliminar os dois booleans abaixo e criar um enum de intervenções
    //@NotNull(message = "Intervention type is required")
    //private MedicalIntervention interventionType;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private Date date;

    @NotNull(message = "Observation is required")
    @Size(max = 999, message = "Observation cannot be longer than 999 characters")
    private String observation;
}
