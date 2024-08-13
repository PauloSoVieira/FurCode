package org.mindera.fur.code.repository.form;

import jdk.jfr.Registered;
import org.mindera.fur.code.model.form.FormFieldAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormFieldAnswerRepository extends JpaRepository<FormFieldAnswer, Long> {
}
