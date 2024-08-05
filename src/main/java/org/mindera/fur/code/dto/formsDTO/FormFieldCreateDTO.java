package org.mindera.fur.code.dto.formsDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormFieldCreateDTO {

    @NotNull(message = "Field name is required")
    private String name;

    @NotNull(message = "Field type is required")
    private String type; // Vai ser enum

}
