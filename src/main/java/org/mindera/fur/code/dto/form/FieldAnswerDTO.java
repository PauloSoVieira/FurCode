package org.mindera.fur.code.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Transfer Object (DTO) representing an answer to a specific field in a form.
 */
@Data
@Schema(description = "Represents an answer to a specific field in a form")
public class FieldAnswerDTO {

    @Schema(description = "The unique identifier of the field", example = "1")
    private Long fieldId;

    @Schema(description = "The answer provided for the field", example = "Yes, I agree")
    private String answer;

}