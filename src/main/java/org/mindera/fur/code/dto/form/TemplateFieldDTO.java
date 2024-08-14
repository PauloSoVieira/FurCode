package org.mindera.fur.code.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a field in a form template.
 */
@Data
public class TemplateFieldDTO {
    @Schema(description = "The unique identifier of the field", example = "1")
    private String fieldIdentifier;
    @Schema(description = "The type of the field", example = "text")
    private String fieldType;
    @Schema(description = "The question of the field", example = "Total Amount")
    private String question;
}
