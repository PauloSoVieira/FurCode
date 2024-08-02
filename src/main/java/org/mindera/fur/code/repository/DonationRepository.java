package org.mindera.fur.code.repository;

import org.mindera.fur.code.model.Donation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRepository extends CrudRepository<Donation, Long> {
}
