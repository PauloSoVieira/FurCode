package org.mindera.fur.code.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToOne
    private PetType petType;

    @ManyToOne
    private Shelter shelter;

    @Column
    private String isAdopted;

    @Column
    private String size; // change to enum later

    @Column
    private Double weight;

    @Column
    private String color;

    @Column
    private int age;

    @Column
    private String observation;

    @Column
    // private String cage;

    @OneToMany(mappedBy = "petId")
    private List<MedicalRecord> medicalRecords;

}
