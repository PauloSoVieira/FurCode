package org.mindera.fur.code.model;


import jakarta.persistence.*;
import lombok.Data;
import org.mindera.fur.code.model.form.AdoptionForm;

import java.util.List;

@Entity
@Data
@Table(name = "pet")
public class Pet {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Shelter shelter;

    @OneToOne
    private PetType pet_type;


    @OneToMany(mappedBy = "pet")
    private List<MedicalRecord> medical_records;

    @OneToMany(mappedBy = "pet")
    private List<AdoptionForm> adoption_forms;
}
