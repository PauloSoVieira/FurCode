package org.mindera.fur.code.dto.forms;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * Data Transfer Object for creating an Adoption Form.
 */
@Data
@Schema(description = "Data Transfer Object for creating an Adoption Form.")
public class AdoptionFormCreateDTO {

    /**
     * Name of the adoption form.
     */
    @Schema(description = "Name of the adoption form.", example = "Adoption Form for John Doe")
    private String name;

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
     * ID of the shelter where the adoption form is located.
     */
    @Schema(description = "ID of the shelter where the adoption form is located.", example = "1")
    @NotNull(message = "Shelter ID is required")
    private Long shelterId;

    /**
     * ID of the person who is adopting the pet.
     */
    @Schema(description = "ID of the person who is adopting the pet.", example = "2")
    @NotNull(message = "Person ID is required")
    private Long personId;

    /**
     * ID of the pet being adopted.
     */
    @Schema(description = "ID of the pet being adopted.", example = "3")
    @NotNull(message = "Pet ID is required")
    private Long petId;

    /**
     * List of fields in the adoption form.
     */
    @Schema(description = "List of fields in the adoption form.")
    private Set<FormFieldDTO1> formFields;

    /**
     * Default constructor.
     */
    public AdoptionFormCreateDTO() {
    }

    /**
     * Constructor with all fields.
     *
     * @param name       The name of the adoption form.
     * @param shelterId  The shelter ID.
     * @param personId   The person ID.
     * @param petId      The pet ID.
     * @param formFields The set of form fields.
     */

    public AdoptionFormCreateDTO(String name, Long shelterId, Long personId, Long petId, Set<FormFieldDTO1> formFields) {
        this.name = name;
        this.shelterId = shelterId;
        this.personId = personId;
        this.petId = petId;
        this.formFields = formFields;


    }

    /**
     * Constructor without form fields.
     *
     * @param name      The name of the adoption form.
     * @param shelterId The shelter ID.
     * @param personId  The person ID.
     * @param petId     The pet ID.
     */
    public AdoptionFormCreateDTO(String name, Long shelterId, Long personId, Long petId) {
        this.name = name;
        this.shelterId = shelterId;
        this.personId = personId;
        this.petId = petId;


    }
}
