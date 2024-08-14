package org.mindera.fur.code.repository.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.pet.PetBreed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Schema(description = "Pet breed repository")
@Repository
public interface PetBreedRepository extends JpaRepository<PetBreed, Long> {

    @Schema(description = "Find pet breed by name")
    PetBreed findByName(String name);
}
