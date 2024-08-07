package org.mindera.fur.code.model.formTest;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "form_fields")
@Data
public class FormField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;
    private String fieldType;  // e.g., "text", "number", "date", etc.

    @OneToMany(mappedBy = "formField")
    private List<FormFieldAnswer> formFieldAnswers = new ArrayList<>();
}