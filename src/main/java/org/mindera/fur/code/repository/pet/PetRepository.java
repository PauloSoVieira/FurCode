package org.mindera.fur.code.repository.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.pet.Pet;
import org.mindera.fur.code.repository.SoftDeleteRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Pet repository.
 */
@Schema(description = "Pet repository")
@Repository
public interface PetRepository extends SoftDeleteRepository<Pet, Long> {

    /**
     * Retrieves all pets, including soft-deleted ones.
     *
     * @return List of all pets.
     */
    @Query("SELECT p FROM Pet p")
    List<Pet> findAllIncludingDeleted();

    /**
     * Retrieves a pet by ID, including soft-deleted ones.
     *
     * @param id The ID of the pet.
     * @return Optional containing the pet if found.
     */
    @Query("SELECT p FROM Pet p WHERE p.id = :id")
    Optional<Pet> findByIdIncludingDeleted(@Param("id") Long id);
}
