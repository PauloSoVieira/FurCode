package org.mindera.fur.code.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "breed")
public class Breed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToOne(mappedBy = "breedId")
    private PetType petType;

}
