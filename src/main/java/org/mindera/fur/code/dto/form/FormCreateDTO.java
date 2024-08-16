package org.mindera.fur.code.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a form to be created.
 */
@Data
@Schema(description = "Represents a form to be created")
public class FormCreateDTO {
    @Schema(description = "The name of the form", example = "Donation Form")
    private String name;
    @Schema(description = "The description of the form", example = "This is a donation form")
    private LocalDateTime createdAt;
    @Schema(description = "The type of the form", example = "donation")
    private String type;
    @Schema(description = "The initial field in the form")
    private FormFieldCreateDTO initialField;
}
