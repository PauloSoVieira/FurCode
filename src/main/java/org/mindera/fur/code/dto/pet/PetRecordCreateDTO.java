package org.mindera.fur.code.dto.pet;

import java.util.Date;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PetRecordCreateDTO {

    @NotNull(message = "Intervention type is required")
    private String petRecordsStatus;

    @NotNull(message = "Observation is required")
    @Size(max = 999, message = "Observation cannot be longer than 999 characters")
    private String observation;

    @Valid
    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private Date date;
}
