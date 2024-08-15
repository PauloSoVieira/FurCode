package org.mindera.fur.code.model;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.mindera.fur.code.model.form.Form;
import org.mindera.fur.code.model.pet.Pet;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

/**
 * Class representing an adoption request.
 */
@Entity
@Table(name = "adoption_request")
@Data
@Tag(name = "Adoption Request", description = "Details about the adoption request entity")
public class AdoptionRequest {


    @Id
    @Positive
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier of the adoption request", example = "1", required = true)
    private Long id;

    /**
     * The id of the pet.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id", nullable = false)
    @Positive
    @Schema(description = "The id of the pet", example = "1", required = true)
    private Pet pet;

    /**
     * The id of the shelter.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shelter_id", nullable = false)
    @Positive
    @Schema(description = "The id of the shelter", example = "1", required = true)
    private Shelter shelter;

    /**
     * The id of the person.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adopter_id", nullable = false)
    @Positive
    @Schema(description = "The id of the person", example = "1", required = true)
    private Person person;

    /**
     * The request details.
     */
    @OneToMany(mappedBy = "adoptionRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "The request details of the adoption request", required = true)
    private Set<RequestDetail> requestDetails;
    @Enumerated(EnumType.STRING)
    @Schema(description = "The state of the adoption request", example = "SENT", required = true)
    private State state;

    @DateTimeFormat
    @Schema(description = "The date of the adoption request", example = "2023-01-01 12:00:00", required = true)
    private Date date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

}
