package org.mindera.fur.code.repository.pet;

import org.mindera.fur.code.model.pet.PetType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetTypeRepository extends JpaRepository<PetType, Long> {
}
