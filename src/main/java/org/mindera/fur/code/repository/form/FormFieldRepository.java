package org.mindera.fur.code.repository.form;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.form.FormField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Schema(name = "Form Field Repository", description = "Repository for managing FormField entities")
@Repository
public interface FormFieldRepository extends JpaRepository<FormField, Long> {
    List<FormField> findByQuestionIn(List<String> questions);

    Optional<FormField> findByQuestion(String question);
}
