package org.mindera.fur.code.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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
    @Schema(description = "The fields in the form", example = "Total Amount, Date, Shelter, Person")
    private List<FormFieldAnswerDTO> formFieldAnswers;
}
