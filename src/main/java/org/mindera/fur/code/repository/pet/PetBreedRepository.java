package org.mindera.fur.code.repository.pet;

import org.mindera.fur.code.model.pet.PetBreed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetBreedRepository extends JpaRepository<PetBreed, Long> {


    PetBreed findByName(String name);
}
