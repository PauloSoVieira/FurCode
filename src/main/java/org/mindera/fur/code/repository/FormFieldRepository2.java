package org.mindera.fur.code.repository;

import org.mindera.fur.code.model.form.FormField1;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface FormFieldRepository2 extends JpaRepository<FormField1, Long> {
    Set<FormField1> findAllByAdoptionFormId(Long id);
}
