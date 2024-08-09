package org.mindera.fur.code.model;


import jakarta.persistence.*;
import lombok.Data;
import org.mindera.fur.code.model.pet.Pet;

import java.util.Set;

@Entity
@Table(name = "adoption_request")
@Data
public class AdoptionRequest {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shelter_id", nullable = false)
    private Shelter shelter;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adopter_id", nullable = false)
    private Person person;

//    @OneToOne
//    private AdoptionForm adoptionForm;

    @OneToMany(mappedBy = "adoptionRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RequestDetail> requestDetails;
}
