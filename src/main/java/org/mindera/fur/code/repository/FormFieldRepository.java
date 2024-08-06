package org.mindera.fur.code.repository;

import org.mindera.fur.code.model.form.FormField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface FormFieldRepository extends JpaRepository<FormField, Long> {
    Set<FormField> findAllByAdoptionFormId(Long id);
}
