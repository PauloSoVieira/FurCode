package org.mindera.fur.code.dto.formsDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class AdoptionFormCreateDTO {

    private String name;
    private Date createdAt;
    private Date updatedAt;
    @NotNull(message = "Shelter ID is required")
    private Long shelterId;
    @NotNull(message = "Person ID is required")
    private Long personId;
    @NotNull(message = "Pet ID is required")
    private Long petId;

    private Set<FormFieldDTO> formFields;


    public AdoptionFormCreateDTO() {
    }

    public AdoptionFormCreateDTO(String name, Long shelterId, Long personId, Long petId, Set<FormFieldDTO> formFields) {
        this.name = name;
        this.shelterId = shelterId;
        this.personId = personId;
        this.petId = petId;
        this.formFields = formFields;


    }

    public AdoptionFormCreateDTO(String name, Long shelterId, Long personId, Long petId) {
        this.name = name;
        this.shelterId = shelterId;
        this.personId = personId;
        this.petId = petId;


    }
}
