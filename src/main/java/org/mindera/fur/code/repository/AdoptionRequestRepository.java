package org.mindera.fur.code.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.AdoptionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AdoptionRequest entity.
 */
@Repository
@Schema(description = "The adoption request repository")
public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest, Long> {
}
