package org.mindera.fur.code.dto.form;

import lombok.Data;

import java.util.List;

@Data
public class FormTemplateDTO {
    private Long id;
    private String name;
    private String type;
    private List<FormFieldCreateDTO> fields;

}
