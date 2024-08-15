package org.mindera.fur.code.repository.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Pet repository.
 */
@Schema(description = "Pet repository")
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
}
