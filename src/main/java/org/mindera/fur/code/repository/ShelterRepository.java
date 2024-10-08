package org.mindera.fur.code.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Shelter entity.
 */
@Schema(description = "The shelter repository")
@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
}
