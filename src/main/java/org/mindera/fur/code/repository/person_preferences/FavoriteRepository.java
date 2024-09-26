package org.mindera.fur.code.repository.person_preferences;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.model.pet.Pet;
import org.mindera.fur.code.model.person_preferences.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Schema(description = "Favorite repository")
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Schema(description = "Find a favorite by person and pet")
    Optional<Favorite> findByPersonAndPet(Person person, Pet pet);

    @Schema(description = "Find all favorites by person")
    List<Favorite> findByPerson(Person person);
}
