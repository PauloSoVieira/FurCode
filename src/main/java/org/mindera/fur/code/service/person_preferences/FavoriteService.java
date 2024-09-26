package org.mindera.fur.code.service.person_preferences;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.mindera.fur.code.dto.person_preferences.FavoriteDTO;
import org.mindera.fur.code.mapper.person_preferences.FavoriteMapper;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.model.pet.Pet;
import org.mindera.fur.code.model.person_preferences.Favorite;
import org.mindera.fur.code.repository.person_preferences.FavoriteRepository;
import org.mindera.fur.code.service.PersonService;
import org.mindera.fur.code.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final PersonService personService;
    private final PetService petService;

    @Autowired
    public FavoriteService(
            FavoriteRepository favoriteRepository,
            PersonService personService,
            PetService petService
    ) {
        this.favoriteRepository = favoriteRepository;
        this.personService = personService;
        this.petService = petService;
    }

    @Transactional
    public FavoriteDTO addFavorite(@NotNull @Positive Long personId, @NotNull @Positive Long petId) {
        Person person = personService.getPersonEntityById(personId);
        Pet pet = petService.findPetEntityById(petId);

        // Validate Pet availability
        if (Boolean.TRUE.equals(pet.getIsAdopted())) {
            throw new EntityExistsException("Only available pets can be favorited.");
        }
        // Check if favorite already exists
        favoriteRepository.findByPersonAndPet(person, pet)
                .ifPresent(favorite -> {throw new EntityExistsException("Pet is already in favorites.");
        });
        Favorite favorite = new Favorite();
        favorite.setPerson(person);
        favorite.setPet(pet);
        favorite.setFavoritedAt(LocalDateTime.now());
        Favorite savedFavorite = favoriteRepository.save(favorite);

        return FavoriteMapper.INSTANCE.toDto(savedFavorite);
    }

    @Transactional
    public void removeFavorite(@NotNull @Positive Long personId, @NotNull @Positive Long petId) {
        Person person = personService.getPersonEntityById(personId);
        Pet pet = petService.findPetEntityById(petId);

        Favorite favorite = favoriteRepository.findByPersonAndPet(person, pet)
                .orElseThrow(() -> new EntityNotFoundException("Pet is not in favorites")
        );
        favoriteRepository.delete(favorite);
    }

    public List<FavoriteDTO> getFavoritesByPerson(@NotNull @Positive Long personId) {
        Person person = personService.getPersonEntityById(personId);
        List<Favorite> favorites = favoriteRepository.findByPerson(person);
        return favorites.stream().map(FavoriteMapper.INSTANCE::toDto).toList();
    }

    public boolean isFavorite(Long personId, Long petId) {
        Person person = personService.getPersonEntityById(personId);
        Pet pet = petService.findPetEntityById(petId);

        return favoriteRepository.findByPersonAndPet(person, pet).isPresent();
    }
}
