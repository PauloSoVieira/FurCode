package org.mindera.fur.code.model.form;

import jakarta.persistence.*;
import lombok.Data;
import org.mindera.fur.code.model.AdoptionRequest;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "form")
@Data
public class AdoptionForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne(mappedBy = "adoptionForm", cascade = CascadeType.ALL, orphanRemoval = true)
    private AdoptionRequest adoptionRequest;

    private String name;
    private Date createdAt;
    private Date updatedAt;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FormField> formFields;
}
