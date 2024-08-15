package org.mindera.fur.code.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing the answers to a form.
 */
@Data
@Schema(description = "Represents the answers to a form")
public class FormAnswerDTO {
    @Schema(description = "The unique identifier of the form", example = "1")
    private Long formId;
    @Schema(description = "The answers provided for each field in the form", example = "Yes, I agree")
    private List<FieldAnswerDTO> answers;


}
