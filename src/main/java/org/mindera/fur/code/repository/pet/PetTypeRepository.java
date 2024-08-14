package org.mindera.fur.code.repository.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.pet.PetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Schema(description = "Pet type repository")
@Repository
public interface PetTypeRepository extends JpaRepository<PetType, Long> {
}
