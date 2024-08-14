package org.mindera.fur.code.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.RequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the RequestDetail entity.
 */
@Repository
@Schema(description = "The request detail repository")
public interface RequestDetailRepository extends JpaRepository<RequestDetail, Long> {

    /**
     * Find all RequestDetails by adoptionRequestId.
     *
     * @param adoptionRequestId the adoptionRequestId
     * @return the list of RequestDetails
     */
    @Schema(description = "Find all RequestDetails by adoptionRequestId")
    List<RequestDetail> findAllByAdoptionRequestId(Long adoptionRequestId);
}
