package org.mindera.fur.code.model.formTest;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "form_field_answers")
@Data
public class FormFieldAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private Form form;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "form_field_id")
    private FormField formField;

    private String answer;
}