package org.mindera.fur.code.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pet_type")
public class PetType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToOne(mappedBy = "petType")
    private Pet pet;

    @OneToOne
    private Breed breedId;

}
