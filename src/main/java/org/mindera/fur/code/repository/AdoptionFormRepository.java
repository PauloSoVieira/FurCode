package org.mindera.fur.code.repository;

import org.mindera.fur.code.model.form.AdoptionForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionFormRepository extends JpaRepository<AdoptionForm, Long> {
}