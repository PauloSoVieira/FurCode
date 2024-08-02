package org.mindera.fur.code.dto.forms;

import lombok.Data;

@Data
public class FormFieldDTO {
    private Long id;
    private String name;
    private String type; // Consider using an enum for field types

    public FormFieldDTO(Long id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}
