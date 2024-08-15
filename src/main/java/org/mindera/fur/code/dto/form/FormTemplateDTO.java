package org.mindera.fur.code.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a form template.
 */
@Data
@Schema(description = "Represents a form template")
public class FormTemplateDTO {
    @Schema(description = "The unique identifier of the template", example = "1")
    private Long id;
    @Schema(description = "The name of the template", example = "Donation Form")
    private String name;
    @Schema(description = "The description of the template", example = "This is a donation form")
    private String type;
    @Schema(description = "The fields in the template", example = "Total Amount, Date, Shelter, Person")
    private List<FormFieldCreateDTO> fields;

}
