package org.mindera.fur.code.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents a FormFieldAnswer entity in the system.
 * This class stores the answers to specific fields in a form.
 */
@Entity
@Table(name = "form_field_answers")
@Data
@Schema(description = "FormFieldAnswer entity")
public class FormFieldAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the form field answer", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    @Schema(description = "The form to which this field answer belongs")
    private Form form;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "form_field_id")
    @Schema(description = "The form field to which this answer belongs")
    private FormField formField;

    @Schema(description = "The answer to the form field", example = "John Doe")
    private String answer;
}