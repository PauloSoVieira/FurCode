package org.mindera.fur.code.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a FormField entity in the system.
 */
@Entity
@Table(name = "form_field")
@Data
@Schema(description = "FormField entity")
public class FormField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the form field", example = "1")
    private Long id;

    @Schema(description = "Question of the form field", example = "What is your name?")
    private String question;

    @Schema(description = "Type of the form field", example = "TEXT")
    private String fieldType;

    @OneToMany(mappedBy = "formField")
    @Schema(description = "List of form field answers")
    private List<FormFieldAnswer> formFieldAnswers = new ArrayList<>();

    @Schema(description = "Indicates whether the form field is active", example = "true")
    private boolean active = true;


    @ManyToOne
    @JoinColumn(name = "form_id")
    @Schema(description = "The form to which this field belongs")
    private Form form;
}