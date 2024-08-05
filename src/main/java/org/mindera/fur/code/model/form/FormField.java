package org.mindera.fur.code.model.form;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "form_field")
@Data
public class FormField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "formFields")
    private Set<AdoptionForm> adoptionForms;

    private String name;
    //Criar Enum para os tipos de campos e tirar Strng para o type
    private String type; // TODO: enum de numbers, text, etc

}
