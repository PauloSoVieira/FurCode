package org.mindera.fur.code.repository.form;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.form.FormFieldAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Schema(name = "Form Field Answer Repository", description = "Repository for managing FormFieldAnswer entities")
@Repository
public interface FormFieldAnswerRepository extends JpaRepository<FormFieldAnswer, Long> {
}
