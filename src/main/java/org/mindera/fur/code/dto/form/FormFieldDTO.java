package org.mindera.fur.code.dto.form;

import lombok.Data;

@Data
public class FormFieldDTO {
    private Long id;
    private String question;
    private String fieldType;
}