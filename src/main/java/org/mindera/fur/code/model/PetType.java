package org.mindera.fur.code.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "pet_type")
@Data
public class PetType {

    @Id
    private Long id;

    private String name;


    @OneToOne(mappedBy = "pet_type")
    private Pet pet;

    @OneToOne
    private Breed breed;


}
