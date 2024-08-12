package org.mindera.fur.code.repository.formTest;

import org.mindera.fur.code.model.form.FormTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface FormTemplateRepository extends JpaRepository<FormTemplate, Long> {
}
