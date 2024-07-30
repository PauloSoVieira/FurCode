package org.mindera.fur.code.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "pet")
public class Pet {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    private Shelter shelter;

    @OneToOne
    private PetType pet_type;
}
