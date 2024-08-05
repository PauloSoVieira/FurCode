package org.mindera.fur.code.dto.forms;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for creating a new FormField.
 */
@Data
public class FormFieldCreateDTO {

    /**
     * The name of the FormField. Must not be null.
     */
    @NotNull(message = "Field name is required")
    private String name;

    /**
     * The type of the FormField. Must not be null. This will be an enum in the future.
     */
    @NotNull(message = "Field type is required")
    private String type;

    /**
     * The ID of the AdoptionForm this FormField belongs to.
     * This is required to establish the association between FormField and AdoptionForm.
     */
    @NotNull(message = "Adoption Form ID is required")
    private Long adoptionFormId;

    /**
     * Default constructor.
     */
    public FormFieldCreateDTO() {
    }

    /**
     * Parameterized constructor for creating a FormFieldCreateDTO.
     *
     * @param name           the name of the FormField
     * @param type           the type of the FormField
     * @param adoptionFormId the ID of the AdoptionForm this FormField belongs to
     */
    public FormFieldCreateDTO(String name, String type, Long adoptionFormId) {
        this.name = name;
        this.type = type;
        this.adoptionFormId = adoptionFormId;
    }


    public FormFieldCreateDTO(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
