package org.mindera.fur.code.repository.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.pet.PetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Schema(description = "Pet record repository")
@Repository
public interface PetRecordRepository extends JpaRepository<PetRecord, Long> {
}
