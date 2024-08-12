package org.mindera.fur.code.dto.form;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FormDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private String type;  // New field

    private List<FormFieldAnswerDTO> formFieldAnswers;
}