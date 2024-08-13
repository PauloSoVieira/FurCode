package org.mindera.fur.code.dto.pet;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PetRecordCreateDTO {

    @NotBlank(message = "Intervention type is required")
    @Size(max = 999, message = "Intervention type cannot be longer than 999 characters")
    @Column(length = 999)
    private String intervention;

    @Valid
    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    @Column(nullable = false)
    private Date createdAt;

    @Valid
    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    @Column(nullable = false)
    private Date updatedAt;
}
