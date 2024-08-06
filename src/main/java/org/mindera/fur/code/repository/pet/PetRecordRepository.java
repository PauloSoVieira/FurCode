package org.mindera.fur.code.repository.pet;

import org.mindera.fur.code.model.pet.PetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRecordRepository extends JpaRepository<PetRecord, Long> {
}
