package org.mindera.fur.code.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.Donation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Donation entity.
 */
@Schema(description = "The donation repository")
@Repository
public interface DonationRepository extends CrudRepository<Donation, Long> {

}
