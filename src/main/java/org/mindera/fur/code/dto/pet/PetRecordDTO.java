package org.mindera.fur.code.dto.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO class for a pet record.
 */
@Data
@NoArgsConstructor
@Schema(description = "A pet record")
public class PetRecordDTO implements Serializable {

    @Positive(message = "ID must be greater than 0")
    @NotNull(message = "ID is required")
    @Schema(description = "The ID of the pet record", example = "1")
    private Long id;

    @Positive(message = "Pet ID must be greater than 0")
    @NotNull(message = "Pet ID is required")
    @Schema(description = "The ID of the pet", example = "1")
    private Long petId;

    @NotBlank(message = "Intervention is required")
    @Size(max = 999, message = "Intervention cannot be longer than 999 characters")
    @Schema(description = "The intervention of the pet record", example = "Pet was washed")
    private String intervention;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    @Schema(description = "The date of the pet record", example = "2024-08-15T11:08:13.990Z")
    private LocalDateTime createdAt;
}
