package org.mindera.fur.code.model.form;

import jakarta.persistence.*;
import lombok.Data;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.model.Pet;
import org.mindera.fur.code.model.Shelter;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "form")
@Data
public class AdoptionForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Person person;


    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;


    private String name;
    private Date createdAt;
    private Date updatedAt;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FormField> formFields;
}
