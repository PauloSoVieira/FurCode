package org.mindera.fur.code.dto.formTESTEDTO;

import lombok.Data;

import java.util.List;

@Data
public class FormTemplateDTO {
    private String name;
    private String type;
    private List<FormFieldCreateDTO> fields;
}
