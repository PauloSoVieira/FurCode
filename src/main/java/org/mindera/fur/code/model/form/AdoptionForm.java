package org.mindera.fur.code.model.form;

import jakarta.persistence.*;
import lombok.Data;
import org.mindera.fur.code.model.AdoptionRequest;

import java.util.Date;
import java.util.Set;


/**
 * Entity class representing an adoption form.
 */
//@Schema(description = "Unique identifier for the adoption form.", example = "1")

@Entity
@Table(name = "form")
@Data
public class AdoptionForm {

    /**
     * Unique identifier for the adoption form.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The associated adoption request.
     * Mapped by 'adoptionForm' in AdoptionRequest class.
     * Cascade type ALL and orphanRemoval enabled.
     */
    //    @Schema(description = "The associated adoption request.")
    @OneToOne(mappedBy = "adoptionForm", cascade = CascadeType.ALL, orphanRemoval = true)
    private AdoptionRequest adoptionRequest;

    /**
     * Name of the adoption form.
     */
//    @Schema(description = "Name of the adoption form.", example = "Adoption Form for John Doe")
    private String name;
    /**
     * The date when the form was created.
     */
    //    @Schema(description = "The date when the form was created.", example = "2023-01-01T12:00:00Z")
    private Date createdAt;
    /**
     * The date when the form was last updated.
     */
    //    @Schema(description = "The date when the form was last updated.", example = "2023-01-01T12:00:00Z")
    private Date updatedAt;
    /**
     * Identifier for the shelter related to this adoption form.
     */
//    @Schema(description = "Identifier for the shelter related to this adoption form.", example = "1")
    private Long shelterId;
    /**
     * Identifier for the person related to this adoption form.
     */
    // @Schema(description = "Identifier for the person related to this adoption form.", example = "1")
    private Long personId;
    /**
     * Identifier for the pet related to this adoption form.
     */
//@Schema(description = "Identifier for the pet related to this adoption form.", example = "1")
    private Long petId;

    /**
     * A set of form fields associated with the adoption form.
     * Fetch type is EAGER.
     */
//@Schema(description = "A set of form fields associated with the adoption form.")
    @OneToMany(mappedBy = "adoptionForm", fetch = FetchType.EAGER)
    private Set<FormField> formFields;


    /**
     * Returns a string representation of the AdoptionForm object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "AdoptionForm{" +
                "id=" + id +
                ", adoptionRequest=" + adoptionRequest +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", shelterId=" + shelterId +
                ", personId=" + personId +
                ", petId=" + petId +
                ", formFields=" + formFields +
                '}';
    }
}
