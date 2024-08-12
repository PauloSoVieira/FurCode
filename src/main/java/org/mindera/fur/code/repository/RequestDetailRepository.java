package org.mindera.fur.code.repository;

import org.mindera.fur.code.model.RequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the RequestDetail entity.
 */
@Repository
public interface RequestDetailRepository extends JpaRepository<RequestDetail, Long> {

    /**
     * Find all RequestDetails by adoptionRequestId.
     *
     * @param adoptionRequestId the adoptionRequestId
     * @return the list of RequestDetails
     */
    List<RequestDetail> findAllByAdoptionRequestId(Long adoptionRequestId);
}
