package org.mindera.fur.code.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
@Schema(description = "The person repository")
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Schema(description = "Find a person by email")
    UserDetails findByEmail(String login);
}
