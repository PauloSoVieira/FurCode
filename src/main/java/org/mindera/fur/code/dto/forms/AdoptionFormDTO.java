package org.mindera.fur.code.dto.forms;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.Set;


/**
 * Data Transfer Object for an Adoption Form.
 */
@Schema(description = "Data Transfer Object for an Adoption Form.")
@Data
public class AdoptionFormDTO {
    /**
     * ID of the adoption form.
     */
    @Schema(description = "ID of the adoption form.", example = "1")
    private Long id;

    /**
     * Name of the adoption form.
     */
    @Schema(description = "Name of the adoption form.", example = "Adoption Form for Jojo")
    private String name;

    /**
     * ID of the shelter where the adoption form is located.
     */
    @Schema(description = "ID of the shelter where the adoption form is located.", example = "1")
    private Long shelterId;

    /**
     * ID of the person who is adopting the pet.
     */
    @Schema(description = "ID of the person who is adopting the pet.", example = "2")
    private Long personId;

    /**
     * ID of the pet being adopted.
     */
    @Schema(description = "ID of the pet being adopted.", example = "3")
    private Long petId;

    /**
     * List of fields in the adoption form.
     */
    @Schema(description = "List of fields in the adoption form.")
    private Set<FormFieldDTO> formFields;

    /**
     * Date and time when the adoption form was created.
     */
    @Schema(description = "Date and time when the adoption form was created.", example = "2023-01-01T00:00:00.000Z")
    private Date createdAt;

    /**
     * Date and time when the adoption form was last updated.
     */
    @Schema(description = "Date and time when the adoption form was last updated.", example = "2023-01-01T00:00:00.000Z")
    private Date updatedAt;

    /**
     * Constructor for AdoptionFormDTO.
     *
     * @param name       The name of the adoption form.
     * @param petId      The ID of the pet being adopted.
     * @param shelterId  The ID of the shelter where the adoption form is located.
     * @param personId   The ID of the person who is adopting the pet.
     * @param formFields The set of form fields in the adoption form.
     */
    public AdoptionFormDTO(String name, Long petId, Long shelterId, Long personId, Set<FormFieldDTO> formFields) {
        this.name = name;
        this.petId = petId;
        this.shelterId = shelterId;
        this.personId = personId;
        this.formFields = formFields;
    }
}
