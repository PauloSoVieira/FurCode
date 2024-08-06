package org.mindera.fur.code.dto.forms;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for FormField.
 * <p>
 * Represents a FormField with an ID, name, and type.
 * The type may be represented as an enum in future versions.
 */
@Data
public class FormFieldDTO {

    /**
     * Unique identifier for the FormField.
     */
    private Long id;

    /**
     * The name of the FormField.
     */
    private String name;

    /**
     * The type of the FormField. Consider using an enum to represent different types.
     */
    private String type;

    /**
     * Default constructor.
     */
    public FormFieldDTO() {
    }

    /**
     * Parameterized constructor for creating a FormFieldDTO with specified values.
     *
     * @param id   the unique identifier of the FormField
     * @param name the name of the FormField
     * @param type the type of the FormField
     */
    public FormFieldDTO(Long id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}
