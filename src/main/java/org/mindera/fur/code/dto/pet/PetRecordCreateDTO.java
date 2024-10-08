package org.mindera.fur.code.dto.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * A DTO class for creating a pet record.
 */
@Data
public class PetRecordCreateDTO {

    @NotBlank(message = "Intervention is required")
    @Size(max = 999, message = "Intervention cannot be longer than 999 characters")
    @Schema(description = "The intervention of the pet record", example = "Pet was sprayed")
    private String intervention;
}
