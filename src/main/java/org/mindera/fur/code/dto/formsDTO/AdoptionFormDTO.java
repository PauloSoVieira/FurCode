package org.mindera.fur.code.dto.formsDTO;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class AdoptionFormDTO {
    private Long id;
    private String name;
    private Long shelterId;
    private Long personId;
    private Long petId;
    private Set<FormFieldDTO> formFields;
    private Date createdAt;
    private Date updatedAt;

    public AdoptionFormDTO(String jojo, long l, long l1, long l2, Set<FormFieldDTO> formFieldDTOS) {
        this.name = jojo;
        this.petId = l;
        this.shelterId = l1;
        this.personId = l2;
        this.formFields = formFieldDTOS;
    }
}
