package org.mindera.fur.code.service.pet;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.mindera.fur.code.dto.external_apis.dog_api.DogBreedDTO;
import org.mindera.fur.code.dto.pet.PetBreedCreateDTO;
import org.mindera.fur.code.dto.pet.PetBreedDTO;
import org.mindera.fur.code.mapper.pet.PetBreedMapper;
import org.mindera.fur.code.model.pet.PetBreed;
import org.mindera.fur.code.model.pet.PetType;
import org.mindera.fur.code.repository.pet.PetBreedRepository;
import org.mindera.fur.code.repository.pet.PetTypeRepository;
import org.mindera.fur.code.service.external_apis.DogApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

    // TODO: Check if is a DOG, CAT or other type of pet and if exist in the database
    @Transactional
    public PetBreedDTO addOrFetchBreed(@Valid PetBreedCreateDTO petBreedCreateDTO) {
        // First, check if the breed already exists in the local database
        PetBreed existingBreed = petBreedRepository.findByName(petBreedCreateDTO.getName());
        if (existingBreed != null) {
            return PetBreedMapper.INSTANCE.toDTO(existingBreed);
        }

        // If not found, query the external API for the breed information
        DogBreedDTO dogBreedDTO = dogApiService.getBreedByName(petBreedCreateDTO.getName());
        if (dogBreedDTO == null) {
            throw new EntityNotFoundException("Breed not found in external API with name: " + petBreedCreateDTO.getName());
        }

        // Create a new PetBreed entity and save it to the local database
        PetBreed newBreed = new PetBreed();
        newBreed.setExternalApiId(dogBreedDTO.getId());
        newBreed.setName(dogBreedDTO.getName());
        newBreed.setDescription(dogBreedDTO.getDescription());

        // Save the new breed to the database
        petBreedRepository.save(newBreed);

        // Now create a new PetType associated with this breed
        PetType petType = new PetType();
        petType.setSpecies(petBreedCreateDTO.getSpecies());
        petType.setBreed(newBreed);
        petTypeRepository.save(petType);

        return PetBreedMapper.INSTANCE.toDTO(newBreed);
    }
}
