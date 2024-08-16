package org.mindera.fur.code.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Person entity.
 */
@Repository
@Schema(description = "The person repository")
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Schema(description = "Find a person by email")
    Person findByEmail(String login);

}
