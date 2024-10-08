package org.mindera.fur.code.dto.form;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a form field to be created.
 */
@Data

public class FormFieldCreateDTO {
    private String question;
    private String fieldType;

    public FormFieldCreateDTO(String fieldType, String question) {
        this.question = question;
        this.fieldType = fieldType;
    }

    public FormFieldCreateDTO() {
    }
}