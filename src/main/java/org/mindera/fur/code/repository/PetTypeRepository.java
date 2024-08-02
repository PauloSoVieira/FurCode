package org.mindera.fur.code.repository;

import org.mindera.fur.code.model.PetType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetTypeRepository extends JpaRepository<PetType, Long> {
}
