package org.mindera.fur.code.repository;


import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.Shelter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Shelter entity.
 */
@Schema(description = "The shelter repository")
@Repository
public interface ShelterRepository extends SoftDeleteRepository<Shelter, Long> {

    /**
     * Retrieves all shelters, including soft-deleted ones.
     *
     * @return List of all shelters.
     */
    @Query("SELECT s FROM Shelter s")
    List<Shelter> findAllIncludingDeleted();

    /**
     * Retrieves a shelter by ID, including soft-deleted ones.
     *
     * @param id The ID of the shelter.
     * @return Optional containing the shelter if found.
     */
    @Query("SELECT s FROM Shelter s WHERE s.id = :id")
    Optional<Shelter> findByIdIncludingDeleted(@Param("id") Long id);
}
