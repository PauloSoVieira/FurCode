package org.mindera.fur.code.model.form;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "form_field")
@Data
public class FormField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private AdoptionForm form;

    private String name;
    private String type; // TODO: enum de numbers, text, etc
    private String label;
    private String placeholder;
//    private Boolean required;
//    private Boolean unique;
}
