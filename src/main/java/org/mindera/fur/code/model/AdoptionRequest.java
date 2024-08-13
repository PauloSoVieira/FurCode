package org.mindera.fur.code.model;


import jakarta.persistence.*;
import lombok.Data;
import org.mindera.fur.code.model.pet.Pet;

import java.util.Set;

/**
 * Class representing an adoption request.
 */
@Entity
@Table(name = "adoption_request")
@Data
public class AdoptionRequest {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The id of the pet.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    /**
     * The id of the shelter.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shelter_id", nullable = false)
    private Shelter shelter;

    /**
     * The id of the person.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adopter_id", nullable = false)
    private Person person;

    /**
     * The request details.
     */
    @OneToMany(mappedBy = "adoptionRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RequestDetail> requestDetails;
}
