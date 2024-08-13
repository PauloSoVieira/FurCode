package org.mindera.fur.code.repository;

import org.mindera.fur.code.model.AdoptionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AdoptionRequest entity.
 */
@Repository
public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest, Long> {
}
