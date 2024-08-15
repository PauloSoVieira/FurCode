package org.mindera.fur.code.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a field answer in a form.
 */
@Data
@Schema(description = "Represents a field answer in a form")
public class FormFieldAnswerDTO {
    @Schema(description = "The unique identifier of the field answer", example = "1")
    private Long id;
    @Schema(description = "The unique identifier of the form", example = "1")
    private Long formId;
    @Schema(description = "The unique identifier of the field", example = "1")
    private Long formFieldId;
    @Schema(description = "The question of the field answer", example = "Total Amount")
    private String question;
    @Schema(description = "The answer of the field answer", example = "20")
    private String answer;
}