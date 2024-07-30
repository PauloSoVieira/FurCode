package org.mindera.fur.code.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "breed")
@Data
public class Breed {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @OneToOne(mappedBy = "breed")
    private PetType pet_type;


}
