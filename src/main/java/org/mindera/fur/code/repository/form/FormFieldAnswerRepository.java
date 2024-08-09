package org.mindera.fur.code.repository.form;

import jdk.jfr.Registered;
import org.mindera.fur.code.model.form.FormFieldAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
@Registered
public interface FormFieldAnswerRepository extends JpaRepository<FormFieldAnswer, Long> {
}
