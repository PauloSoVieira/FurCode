package org.mindera.fur.code.repository;

import org.mindera.fur.code.model.Breed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreedRepository extends JpaRepository<Breed, Long> {
}
