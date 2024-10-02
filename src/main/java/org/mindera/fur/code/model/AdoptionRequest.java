package org.mindera.fur.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindera.fur.code.model.form.Form;
import org.mindera.fur.code.model.pet.Pet;

import java.util.HashSet;
import java.util.Set;

/**
 * Class representing an adoption request.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "adoption_request")
public class AdoptionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "shelter_id", nullable = false)
    private Shelter shelter;

    @ManyToOne
    @JoinColumn(name = "adopter_id", nullable = false)
    private Person person;

    @OneToOne
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @OneToMany(mappedBy = "adoptionRequest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<RequestDetail> requestDetails = new HashSet<>();
}
