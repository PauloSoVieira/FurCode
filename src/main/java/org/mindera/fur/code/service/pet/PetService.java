package org.mindera.fur.code.service.pet;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.mindera.fur.code.dto.pet.*;
import org.mindera.fur.code.mapper.pet.PetMapper;
import org.mindera.fur.code.mapper.pet.PetRecordMapper;
import org.mindera.fur.code.mapper.pet.PetUpdateMapper;
import org.mindera.fur.code.messages.pet.PetMessages;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.pet.Pet;
import org.mindera.fur.code.model.pet.PetRecord;
import org.mindera.fur.code.model.pet.PetType;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.pet.PetRecordRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.mindera.fur.code.repository.pet.PetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;

/**
 * Pet Service class for managing Pet entities.
 */
@Validated
@Service
public class PetService {
    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;
    private final PetRecordRepository petRecordRepository;
    private final ShelterRepository shelterRepository;

    @Autowired
    public PetService(
            PetRepository petRepository,
            PetRecordRepository petRecordRepository,
            PetTypeRepository petTypeRepository,
            ShelterRepository shelterRepository
    ) {
        this.petRepository = petRepository;
        this.petRecordRepository = petRecordRepository;
        this.petTypeRepository = petTypeRepository;
        this.shelterRepository = shelterRepository;
    }

    /**
     * Find all pets.
     *
     * @return a list of all pets
     */
    public List<PetDTO> findAllPets() {
        List<Pet> pets = petRepository.findAll();
        return pets.stream().map(PetMapper.INSTANCE::toDTO).toList();
    }
    
    /**
     * Find a pet by ID.
     *
     * @param id The ID of the pet.
     * @return The pet.
     */
    public PetDTO findPetById(@Valid Long id) {
        Pet pet = findAndAssignPet(id);
        return PetMapper.INSTANCE.toDTO(pet);
    }
    
    /**
     * Add a pet.
     *
     * @param petCreateDTO The pet to add.
     * @return The pet.
     */
    @Transactional
    public PetDTO addPet(@Valid PetCreateDTO petCreateDTO) {
        Pet pet = PetMapper.INSTANCE.toModel(petCreateDTO);

        pet.setPetType(findAndAssignPetType(petCreateDTO.getPetTypeId()));
        pet.setShelter(findAndAssignShelter(petCreateDTO.getShelterId()));

        pet = petRepository.save(pet);
        return PetMapper.INSTANCE.toDTO(pet);
    }

    /**
     * Update a pet.
     *<p>
     * <b>Note:</b> The pet.setId(id) is required for MapStruct to work.
     * It will be removed in the future from the service and implemented in the mapper, once I figure out how to do it.
     * </p>
     * @param id The ID of the pet.
     * @param petUpdateDTO The pet to update.
     */
    @Transactional
    public void updatePet(@Valid Long id, @Valid PetUpdateDTO petUpdateDTO) {
        Pet pet =  findAndAssignPet(id);

        PetUpdateMapper.INSTANCE.updatePetFromDto(petUpdateDTO, pet);

        pet.setId(id);
        petRepository.save(pet);
    }

    /**
     * Deletes a pet by its ID.
     * <p>
     * <b>Note:</b> This method currently performs a hard delete (removes the record from the database).
     * In a future release, this method will be updated to perform a soft delete, which will mark the record as deleted without removing it.
     * Also, currently will delete all pet records from the database. This not what we want.
     * </p>
     *
     * @param id the ID of the pet to be deleted
     * @throws EntityNotFoundException if the pet with the specified ID is not found
     */
    public void softDeletePet(@Valid Long id) {
        Pet pet = findAndAssignPet(id);
        petRepository.delete(pet);
    }

    /**
     * Add a pet record.
     *
     * @param id The ID of the pet.
     * @param petRecordCreateDTO The pet record to add.
     * @return The pet record.
     */
    @Transactional
    public PetRecordDTO addPetRecord(@Valid Long id, @Valid PetRecordCreateDTO petRecordCreateDTO) {
        Pet pet = findAndAssignPet(id);

        PetRecord petRecord = PetRecordMapper.INSTANCE.toModel(petRecordCreateDTO);

        petRecord.setPet(pet);
        petRecord = petRecordRepository.save(petRecord);
        return PetRecordMapper.INSTANCE.toDTO(petRecord);
    }

    /**
     * Get all pet records by pet ID.
     *
     * @param id The ID of the pet.
     * @return A list of pet records.
     */
    public List<PetRecordDTO> getAllPetRecordsByPetId(@Valid Long id) {
        Pet pet = findAndAssignPet(id);
        return pet.getPetRecords().stream().map(PetRecordMapper.INSTANCE::toDTO).toList();
    }

    /**
     * Deletes all pets and related records.
     * <p>
     * <b>Note:</b> This method is for testing purposes only and should not be used in production.
     * It deletes all pets and pet records and should be handled with caution.
     * </p>
     */
    @Transactional
    public void deleteAllPets() {
        petRepository.deleteAll();
        petRecordRepository.deleteAll();
    }

    private Pet findAndAssignPet(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PetMessages.PET_NOT_FOUND + id));
    }

    private Shelter findAndAssignShelter(Long id) {
        return shelterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PetMessages.SHELTER_NOT_FOUND + id));
    }

    private PetType findAndAssignPetType(Long id) {
        return petTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PetMessages.PET_TYPE_NOT_FOUND + id));
    }
}
