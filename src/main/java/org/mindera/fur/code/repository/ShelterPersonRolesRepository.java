package org.mindera.fur.code.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.model.ShelterPersonRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the ShelterPersonRoles entity.
 */
@Repository
@Schema(description = "The shelter person roles repository")
public interface ShelterPersonRolesRepository extends JpaRepository<ShelterPersonRoles, Long> {

    @Query("SELECT spr.person FROM ShelterPersonRoles spr WHERE spr.shelter.id = :shelterId")
    List<Person> findPersonsByShelterId(@Param("shelterId") Long shelterId);
}
