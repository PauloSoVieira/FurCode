package org.mindera.fur.code.dto.form;

import lombok.Data;

@Data
public class FormFieldAnswerDTO {
    private Long id;
    private Long formId;
    private Long formFieldId;
    private String question;
    private String answer;
}