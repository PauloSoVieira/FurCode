package org.mindera.fur.code.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "donation")
@Data
public class Donation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

}
