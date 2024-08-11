package org.mindera.fur.code.repository.form;

import org.mindera.fur.code.model.form.FormField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormFieldRepository extends JpaRepository<FormField, Long> {
    List<FormField> findByQuestionIn(List<String> questions);

    Optional<FormField> findByQuestion(String question);
}
