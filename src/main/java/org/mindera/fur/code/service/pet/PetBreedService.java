package org.mindera.fur.code.service.pet;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.mindera.fur.code.dto.external_apis.dog_api.DogBreedDTO;
import org.mindera.fur.code.dto.pet.PetBreedCreateDTO;
import org.mindera.fur.code.dto.pet.PetBreedDTO;
import org.mindera.fur.code.mapper.pet.PetBreedMapper;
import org.mindera.fur.code.model.enums.pet.PetSpeciesEnum;
import org.mindera.fur.code.model.pet.PetBreed;
import org.mindera.fur.code.model.pet.PetType;
import org.mindera.fur.code.repository.pet.PetBreedRepository;
import org.mindera.fur.code.repository.pet.PetTypeRepository;
import org.mindera.fur.code.service.external_apis.DogApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Pet Breed Service class for managing PetBreed entities.
 */
@Validated
@Service
public class PetBreedService {
    private final PetTypeRepository petTypeRepository;
    private final PetBreedRepository petBreedRepository;
    private final DogApiService dogApiService;

    @Autowired
    public PetBreedService(
            PetTypeRepository petTypeRepository,
            PetBreedRepository petBreedRepository,
            DogApiService dogApiService
    ) {
        this.petTypeRepository = petTypeRepository;
        this.petBreedRepository = petBreedRepository;
        this.dogApiService = dogApiService;
    }

    /**
     * Fetch a pet breed from external API if not exist in local database and save to local repository.
     *
     * @param petBreedCreateDTO the pet breed to create or fetch
     * @return the created or fetched pet breed
     */
    @Transactional
    public PetBreedDTO addOrFetchBreed(@Valid PetBreedCreateDTO petBreedCreateDTO) {
        // Check if the breed already exists in the local database
        PetBreed existingBreed = findExistingBreed(petBreedCreateDTO.getName());
        if (existingBreed != null) {
            return PetBreedMapper.INSTANCE.toDTO(existingBreed);
        }

        // Check if the species is "Dog" before proceeding
        verifySpecies(petBreedCreateDTO.getSpecies());

        // If not found, query the external API for the breed information
        DogBreedDTO dogBreedDTO = fetchBreedFromExternalApi(petBreedCreateDTO.getName());

        // Create a new PetBreed entity and save it to the local database
        PetBreed newBreed = createAndSavePetBreed(dogBreedDTO);

        // Now create a new PetType associated with this breed
        createAndSavePetType(petBreedCreateDTO, newBreed);

        return PetBreedMapper.INSTANCE.toDTO(newBreed);
    }

    private void createAndSavePetType(PetBreedCreateDTO petBreedCreateDTO, PetBreed newBreed) {
        PetType petType = new PetType();
        petType.setSpecies(petBreedCreateDTO.getSpecies());
        petType.setBreed(newBreed);
        petTypeRepository.save(petType);
    }

    @CachePut(value = "petBreeds", key = "#newBreed.name")
    @NotNull
    private PetBreed createAndSavePetBreed(DogBreedDTO dogBreedDTO) {
        PetBreed newBreed = new PetBreed();
        newBreed.setExternalApiId(dogBreedDTO.getId());
        newBreed.setName(dogBreedDTO.getName());
        newBreed.setDescription(dogBreedDTO.getDescription());
        petBreedRepository.save(newBreed);
        return newBreed;
    }

    @NotNull
    private DogBreedDTO fetchBreedFromExternalApi(String breedName) {
        DogBreedDTO dogBreedDTO = dogApiService.getBreedByName(breedName);
        if (dogBreedDTO == null) {
            throw new EntityNotFoundException("Breed not found in external API with name: " + breedName);
        }
        return dogBreedDTO;
    }

    private static void verifySpecies(PetSpeciesEnum species) {
        if (!species.equals(PetSpeciesEnum.DOG)) {
            throw new UnsupportedOperationException("Currently, only Dog breeds are supported. Provided species: " + species.getName());
        }
    }

    @Cacheable(value = "petBreeds", key = "#name", unless = "#result == null")
    private PetBreed findExistingBreed(String name) {
        return petBreedRepository.findByName(name);
    }
}
