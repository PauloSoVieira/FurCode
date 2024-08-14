package org.mindera.fur.code.repository.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.pet.PetCage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Schema(description = "Pet cage repository")
@Repository
public interface PetCageRepository extends JpaRepository<PetCage, Long> {
}
