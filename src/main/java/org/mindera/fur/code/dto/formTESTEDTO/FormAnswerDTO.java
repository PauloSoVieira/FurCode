package org.mindera.fur.code.dto.formTESTEDTO;

import lombok.Data;

import java.util.List;

@Data
public class FormAnswerDTO {
    private Long formId;
    private List<FieldAnswerDTO> answers;

}
