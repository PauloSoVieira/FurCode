package org.mindera.fur.code.repository;

import org.mindera.fur.code.model.AdoptionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest, Long> {
}
