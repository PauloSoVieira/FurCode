package org.mindera.fur.code.repository.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.pet.PetRecord;
import org.mindera.fur.code.repository.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Pet record repository.
 */
@Schema(description = "Pet record repository")
@Repository
public interface PetRecordRepository extends SoftDeleteRepository<PetRecord, Long> {

    /**
     * Retrieves all soft-deleted pet records associated with a specific pet ID.
     *
     * @param petId The ID of the pet.
     * @return List of soft-deleted PetRecords.
     */
    List<PetRecord> findAllByPet_IdAndDeletedAtIsNotNull(Long petId);
}
