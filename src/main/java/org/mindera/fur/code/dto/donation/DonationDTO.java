package org.mindera.fur.code.dto.donation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class DonationDTO {

    //@Schema(type = "long", example = "1000")
    @NotNull
    @NotBlank(message = "Donation id is required")
    private Long id;
    
    //@Schema(type = "double", example = "20")
    @NotNull
    @NotBlank(message = "Donation amount is required")
    @Min(value = 0, message = "Donation amount must be greater than 0")
    @Max(value = 999999, message = "Donation amount must be less than 999999")
    private Double total;

    //TODO: add date validation
    //@Schema(type = "date", example = "2024-01-01")
    @NotNull
    @NotBlank(message = "Donation date is required")
    @Valid
    private Date date;

    //@Schema(type = "long", example = "2")
    @NotNull
    @NotBlank(message = "Pet id is required")
    @Valid
    private Long petId;

    //@Schema(type = "long", example = "23")
    @NotNull
    @NotBlank(message = "Person id is required")
    @Valid
    private Long personId;
}
