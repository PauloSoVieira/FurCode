package org.mindera.fur.code.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Form entity in the system.
 */
@Entity
@Table(name = "forms")
@Data
@Schema(description = "Form entity")
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the form", example = "1")
    private Long id;
    @Schema(description = "Name of the form", example = "Customer Feedback")
    private String name;
    @Schema(description = "Type of the form", example = "DEFAULT")
    private String type;


    @Column(name = "created_at")
    @Schema(description = "Date and time when the form was created", example = "2023-01-01T00:00:00")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "List of form field answers")
    private List<FormFieldAnswer> formFieldAnswers = new ArrayList<>();
    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "List of form fields")
    private List<FormField> fields = new ArrayList<>();

    /**
     * Sets the creation timestamp when the entity is persisted.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Adds a form field answer to the form and sets the form reference in the answer.
     *
     * @param answer The FormFieldAnswer to be added
     */
    public void addFormFieldAnswer(FormFieldAnswer answer) {
        formFieldAnswers.add(answer);
        answer.setForm(this);
    }

}