package org.mindera.fur.code.model;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.Data;
import org.mindera.fur.code.model.form.Form;
import org.mindera.fur.code.model.pet.Pet;

import java.util.HashSet;
import java.util.Set;

/**
 * Class representing an adoption request.
 */
@Entity
@Table(name = "adoption_request")
@Data
@Schema(description = "Adoption Request entity")
@Tag(name = "Adoption Request", description = "Details about the adoption request entity")
public class AdoptionRequest {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier of the adoption request", example = "1", required = true)
    private Long id;

    /**
     * The id of the pet.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id", nullable = false)
    @Schema(description = "The id of the pet", example = "1", required = true)
    private Pet pet;

    /**
     * The id of the shelter.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shelter_id", nullable = false)
    @Schema(description = "The id of the shelter", example = "1", required = true)
    private Shelter shelter;

    /**
     * The id of the person.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adopter_id", nullable = false)
    @Schema(description = "The id of the person", example = "1", required = true)
    private Person person;

    /**
     * The request details.
     */
    @OneToMany(mappedBy = "adoptionRequest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Schema(description = "The request details of the adoption request", required = true)
    private Set<RequestDetail> requestDetails = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

}
