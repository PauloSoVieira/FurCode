package org.mindera.fur.code.repository.formTest;

import org.mindera.fur.code.model.formTest.FormField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormFieldRepository extends JpaRepository<FormField, Long> {
    List<FormField> findByQuestionIn(List<String> questions);
}
