package org.mindera.fur.code.model.form;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "form_field")
@Data
public class FormField1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "adoption_form_id")
    private AdoptionForm adoptionForm;

    private String name;
    //Criar Enum para os tipos de campos e tirar Strng para o type
    private String type; // TODO: enum de numbers, text, etc

}