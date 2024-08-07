package org.mindera.fur.code.model;


import jakarta.persistence.*;
import lombok.Data;
import org.mindera.fur.code.model.pet.Pet;

import java.util.Date;

@Entity
@Table(name = "donation")
@Data
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total")
    private Double total;

    @Column(name = "date")
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;


}
