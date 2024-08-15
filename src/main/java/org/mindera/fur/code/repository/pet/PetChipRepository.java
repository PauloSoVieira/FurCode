package org.mindera.fur.code.repository.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.pet.PetChip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Pet chip repository.
 * <p>
 * <b>Note:</b> This repository is currently not used in the application.
 * It is included for future reference and may be used in the future.
 *</p>
 */
@Schema(description = "Pet chip repository")
@Repository
public interface PetChipRepository extends JpaRepository<PetChip, Long> {
}
