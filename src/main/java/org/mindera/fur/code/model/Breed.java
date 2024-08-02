package org.mindera.fur.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "breed")
public class Breed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Breed name must be provided")
    @Column(nullable = false)
    private String name;

    /*
    @NotNull(message = "Animal type must be provided")
    @ManyToOne
    @JoinColumn(name = "pet_type_id", nullable = false)
    private List<PetType> petType;
     */

    //@Valid
    //@OneToMany(mappedBy = "breed", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<PetType> petTypes; // Changed from @OneToOne(mappedBy = "breedId")
}
