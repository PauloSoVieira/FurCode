package org.mindera.fur.code.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.mindera.fur.code.dto.external_apis.dog_api.DogBreedDTO;
import org.mindera.fur.code.dto.pet.*;
import org.mindera.fur.code.mapper.pet.PetBreedMapper;
import org.mindera.fur.code.mapper.pet.PetMapper;
import org.mindera.fur.code.mapper.pet.PetRecordMapper;
import org.mindera.fur.code.messages.pet.PetMessages;
import org.mindera.fur.code.model.pet.Pet;
import org.mindera.fur.code.model.pet.PetBreed;
import org.mindera.fur.code.model.pet.PetRecord;
import org.mindera.fur.code.model.pet.PetType;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.pet.PetBreedRepository;
import org.mindera.fur.code.repository.pet.PetRecordRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.mindera.fur.code.repository.pet.PetTypeRepository;
import org.mindera.fur.code.service.external_apis.DogApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@Validated
@Service
public class PetService {

    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;
    private final PetRecordRepository petRecordRepository;
    private final ShelterRepository shelterRepository;
    private final PetBreedRepository petBreedRepository;
    private final DogApiService dogApiService;

    @Autowired
    public PetService(
            PetRepository petRepository,
            PetRecordRepository petRecordRepository,
            PetTypeRepository petTypeRepository,
            ShelterRepository shelterRepository,
            PetBreedRepository petBreedRepository,
            DogApiService dogApiService
    ) {
        this.petRepository = petRepository;
        this.petRecordRepository = petRecordRepository;
        this.petTypeRepository = petTypeRepository;
        this.shelterRepository = shelterRepository;
        this.petBreedRepository = petBreedRepository;
        this.dogApiService = dogApiService;
    }

    public List<PetDTO> findAllPets() {
        List<Pet> pets = petRepository.findAll();
        return pets.stream().map(PetMapper.INSTANCE::toDTO).toList();
    }

    public PetDTO findPetById(@Valid Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PetMessages.PET_NOT_FOUND + id));
        return PetMapper.INSTANCE.toDTO(pet);
    }

    @Transactional
    public PetDTO addPet(@Valid PetCreateDTO petCreateDTO) {
        Pet pet = PetMapper.INSTANCE.toModel(petCreateDTO);

        pet.setPetType(petTypeRepository.findById(petCreateDTO.getPetTypeId()).orElseThrow(() -> new EntityNotFoundException("Pet type not found with ID: " + petCreateDTO.getPetTypeId())));
        pet.setShelter(shelterRepository.findById(petCreateDTO.getShelterId()).orElseThrow(() -> new EntityNotFoundException("Shelter not found with ID: " + petCreateDTO.getShelterId())));

        pet = petRepository.save(pet);
        return PetMapper.INSTANCE.toDTO(pet);
    }

    @Transactional
    public void updatePet(@Valid Long id, @Valid PetUpdateDTO petUpdateDTO) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PetMessages.PET_NOT_FOUND + id));

        // TODO: Find a way to use the mapper here

        if (petUpdateDTO.getIsAdopted() != null) {
            pet.setIsAdopted(petUpdateDTO.getIsAdopted());
        }
        if (petUpdateDTO.getIsVaccinated() != null) {
            pet.setIsVaccinated(petUpdateDTO.getIsVaccinated());
        }
        if (petUpdateDTO.getSize() != null) {
            pet.setSize(petUpdateDTO.getSize());
        }
        if (petUpdateDTO.getWeight() != null) {
            pet.setWeight(petUpdateDTO.getWeight());
        }
        if (petUpdateDTO.getColor() != null) {
            pet.setColor(petUpdateDTO.getColor());
        }
        if (petUpdateDTO.getAge() != null) {
            pet.setAge(petUpdateDTO.getAge());
        }
        if (petUpdateDTO.getObservations() != null) {
            pet.setObservations(petUpdateDTO.getObservations());
        }

        petRepository.save(pet);
    }

    // TODO: The following method don't do the soft delete, but will be implemented in the future
    public void softDeletePet(@Valid Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PetMessages.PET_NOT_FOUND + id));
        petRepository.delete(pet);
    }

    @Transactional
    public PetRecordDTO addPetRecord(@Valid Long id, @Valid PetRecordCreateDTO petRecordCreateDTO) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PetMessages.PET_NOT_FOUND + id));

        PetRecord petRecord = PetRecordMapper.INSTANCE.toModel(petRecordCreateDTO);
        petRecord.setPet(pet);

        petRecord = petRecordRepository.save(petRecord);
        return PetRecordMapper.INSTANCE.toDTO(petRecord);
    }

    public List<PetRecordDTO> getAllPetRecordsByPetId(@Valid Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PetMessages.PET_NOT_FOUND + id));
        return pet.getPetRecords().stream().map(PetRecordMapper.INSTANCE::toDTO).toList();
    }

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
        newBreed.setName(dogBreedDTO.getName());
        newBreed.setDescription(dogBreedDTO.getDescription());

        // Save the new breed to the database
        petBreedRepository.save(newBreed);

        // Now create a new PetType associated with this breed
        PetType petType = new PetType();
        petType.setType(petBreedCreateDTO.getType());
        petType.setBreed(newBreed);
        petTypeRepository.save(petType);

        return PetBreedMapper.INSTANCE.toDTO(newBreed);
    }

    // For testing purposes only
    @Transactional
    public void deleteAllPets() {
        petRepository.deleteAll();
        petRecordRepository.deleteAll();
    }
}
