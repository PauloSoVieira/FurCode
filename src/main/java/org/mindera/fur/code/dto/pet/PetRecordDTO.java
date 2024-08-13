package org.mindera.fur.code.dto.pet;

import jakarta.persistence.*;
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
