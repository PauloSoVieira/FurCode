package org.mindera.fur.code.dto.formsDTO;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class AdoptionFormDTO {

    private Long id;
    private String name;
    private Set<FormFieldDTO> formFields;
    private Date createdAt;
    private Date updatedAt;

}
