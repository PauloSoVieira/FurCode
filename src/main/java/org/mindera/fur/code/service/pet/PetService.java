package org.mindera.fur.code.service.pet;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.mindera.fur.code.dto.pet.PetCreateDTO;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.mindera.fur.code.dto.pet.PetRecordCreateDTO;
import org.mindera.fur.code.dto.pet.PetRecordDTO;
import org.mindera.fur.code.mapper.pet.PetMapper;
import org.mindera.fur.code.mapper.pet.PetRecordMapper;
import org.mindera.fur.code.messages.pet.PetMessages;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.pet.Pet;
import org.mindera.fur.code.model.pet.PetBreed;
import org.mindera.fur.code.model.pet.PetRecord;
import org.mindera.fur.code.model.pet.PetType;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.pet.PetBreedRepository;
import org.mindera.fur.code.repository.pet.PetRecordRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.mindera.fur.code.repository.pet.PetTypeRepository;
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

    @Autowired
    public PetService(
            PetRepository petRepository,
            PetRecordRepository petRecordRepository,
            PetTypeRepository petTypeRepository,
            ShelterRepository shelterRepository,
            PetBreedRepository petBreedRepository
    ) {
        this.petRepository = petRepository;
        this.petRecordRepository = petRecordRepository;
        this.petTypeRepository = petTypeRepository;
        this.shelterRepository = shelterRepository;
        this.petBreedRepository = petBreedRepository;
    }

    public List<PetDTO> findAllPets() {
        List<Pet> pets = petRepository.findAll();
        return pets.stream().map(PetMapper.INSTANCE::toDTO).toList();
    }

    public PetDTO findPetById(@Valid Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PetMessages.PET_NOT_FOUND + id));
        return PetMapper.INSTANCE.toDTO(pet);
    }

    public PetDTO addPet(@Valid PetCreateDTO petCreateDTO) {
        Pet pet = PetMapper.INSTANCE.toModel(petCreateDTO);

//        PetBreed breed = new PetBreed();
//        breed.setName("Labrador");
//        petBreedRepository.save(breed);
//
//        Shelter shelter = new Shelter();
//        shelter.setName("Shelter");
//        shelterRepository.save(shelter);
//
//        PetType newPetType = new PetType();
//        newPetType.setType(petCreateDTO.getPetTypeId().toString());
//        newPetType.setBreed(petBreedRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("Breed not found with ID: " + 1L)));
//        petTypeRepository.save(newPetType);

        pet.setPetType(petTypeRepository.findById(petCreateDTO.getPetTypeId()).orElseThrow(() -> new EntityNotFoundException("Pet type not found with ID: " + petCreateDTO.getPetTypeId())));
        pet.setShelter(shelterRepository.findById(petCreateDTO.getShelterId()).orElseThrow(() -> new EntityNotFoundException("Shelter not found with ID: " + petCreateDTO.getShelterId())));

        pet = petRepository.save(pet);
        return PetMapper.INSTANCE.toDTO(pet);
    }

    public void updatePet(@Valid Long id, @Valid PetCreateDTO petCreateDTO) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PetMessages.PET_NOT_FOUND + id));

        // Use the mapper to update fields that are not null
        PetMapper.INSTANCE.updatePetFromDTO(petCreateDTO, pet);

        // Update fields conditionally
        pet.setName(petCreateDTO.getName() != null && !petCreateDTO.getName().trim().isEmpty() ? petCreateDTO.getName() : pet.getName());

        pet.setPetType(petCreateDTO.getPetTypeId() != null ?
                petTypeRepository.findById(petCreateDTO.getPetTypeId()).orElseThrow(() -> new EntityNotFoundException("Pet type not found with ID: " + petCreateDTO.getPetTypeId())) :
                pet.getPetType());

        pet.setShelter(petCreateDTO.getShelterId() != null ?
                shelterRepository.findById(petCreateDTO.getShelterId()).orElseThrow(() -> new EntityNotFoundException("Shelter not found with ID: " + petCreateDTO.getShelterId())) :
                pet.getShelter());

        pet.setIsAdopted(petCreateDTO.getIsAdopted() != null ? petCreateDTO.getIsAdopted() : pet.getIsAdopted());
        pet.setSize(petCreateDTO.getSize() != null && !petCreateDTO.getSize().trim().isEmpty() ? petCreateDTO.getSize() : pet.getSize());
        pet.setWeight(petCreateDTO.getWeight() != null ? petCreateDTO.getWeight() : pet.getWeight());
        pet.setColor(petCreateDTO.getColor() != null && !petCreateDTO.getColor().trim().isEmpty() ? petCreateDTO.getColor() : pet.getColor());
        pet.setAge(petCreateDTO.getAge() != null ? petCreateDTO.getAge() : pet.getAge());
        pet.setObservations(petCreateDTO.getObservations() != null && !petCreateDTO.getObservations().trim().isEmpty() ? petCreateDTO.getObservations() : pet.getObservations());
        petRepository.save(pet);
    }

    // TODO: The following method don't do the soft delete, but will be implemented in the future
    public void softDeletePet(@Valid Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PetMessages.PET_NOT_FOUND + id));
        petRepository.delete(pet);
    }

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

    // For testing purposes only
    public void deleteAllPets() {
        petRepository.deleteAll();
        petRecordRepository.deleteAll();
    }
}
