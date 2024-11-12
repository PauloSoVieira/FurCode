package org.mindera.fur.code.service.pet;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.mindera.fur.code.dto.pet.*;
import org.mindera.fur.code.mapper.pet.PetMapper;
import org.mindera.fur.code.mapper.pet.PetRecordMapper;
import org.mindera.fur.code.mapper.pet.PetUpdateMapper;
import org.mindera.fur.code.messages.pet.PetMessages;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.person_preferences.Favorite;
import org.mindera.fur.code.model.pet.Pet;
import org.mindera.fur.code.model.pet.PetRecord;
import org.mindera.fur.code.model.pet.PetType;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.person_preferences.FavoriteRepository;
import org.mindera.fur.code.repository.pet.PetRecordRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.mindera.fur.code.repository.pet.PetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
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
    private final FavoriteRepository favoriteRepository;

    @Autowired
    public PetService(
            PetRepository petRepository,
            PetRecordRepository petRecordRepository,
            PetTypeRepository petTypeRepository,
            ShelterRepository shelterRepository,
            FavoriteRepository favoriteRepository
    ) {
        this.petRepository = petRepository;
        this.petRecordRepository = petRecordRepository;
        this.petTypeRepository = petTypeRepository;
        this.favoriteRepository = favoriteRepository;
        this.shelterRepository = shelterRepository;
    }

    /**
     * Find all active pets.
     *
     * @return a list of all active pets
     */
    @Cacheable(cacheNames = "pets")
    public List<PetDTO> findAllPets() {
        List<Pet> pets = petRepository.findAllActive();
        return pets.stream().map(PetMapper.INSTANCE::toDTO).toList();
    }

    /**
     * Find an active pet by ID.
     *
     * @param id The ID of the pet.
     * @return The pet.
     * @throws EntityNotFoundException if the pet with the specified ID is not found.
     */
    @Cacheable(cacheNames = "pet", key = "#id")
    public PetDTO findPetById(@NotNull @Positive Long id) {
        Pet pet = findActivePetEntityById(id);
        return PetMapper.INSTANCE.toDTO(pet);
    }

    /**
     * Add a new pet.
     *
     * @param petCreateDTO The DTO containing pet creation data.
     * @return The created pet.
     */
    @CachePut(cacheNames = "pet", key = "#result.id")
    @CacheEvict(cacheNames = "pets", allEntries = true)
    @Transactional
    public PetDTO addPet(@Valid PetCreateDTO petCreateDTO) {
        Pet pet = PetMapper.INSTANCE.toModel(petCreateDTO);

        pet.setPetType(findAndAssignPetType(petCreateDTO.getPetTypeId()));
        pet.setShelter(findAndAssignShelter(petCreateDTO.getShelterId()));

        pet = petRepository.save(pet);
        return PetMapper.INSTANCE.toDTO(pet);
    }

    /**
     * Update's a pet's details.
     * <p>
     * <b>Note:</b> The pet.setId(id) is required for MapStruct to work.
     * It will be removed in the future from the service and implemented in the mapper, once I figure out how to do it.
     * </p>
     *
     * @param id           The ID of the pet.
     * @param petUpdateDTO The DTO containing updated pet information.
     * @throws EntityNotFoundException if the pet with the specified ID is not found.
     */
    @CachePut(cacheNames = "pet", key = "#id")
    @CacheEvict(cacheNames = "pets", allEntries = true)
    @Transactional
    public PetDTO updatePet(@NotNull @Positive Long id, @Valid PetUpdateDTO petUpdateDTO) {
        Pet pet = findActivePetEntityById(id);

        removeFavoritesIfAdopted(petUpdateDTO, pet);

        PetUpdateMapper.INSTANCE.updatePetFromDto(petUpdateDTO, pet);

        pet.setId(id);
        pet = petRepository.save(pet);
        return PetMapper.INSTANCE.toDTO(pet);
    }

    /**
     * Soft deletes a pet by setting its deletedAt timestamp.
     *
     * @param id the ID of the pet to be soft deleted
     * @throws EntityNotFoundException if the pet with the specified ID is not found
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = "pet", key = "#id"),
            @CacheEvict(cacheNames = "pets", allEntries = true)
    })
    @Transactional
    public void softDeletePet(@NotNull @Positive Long id) {
        Pet pet = findActivePetEntityById(id);
        LocalDateTime deletedAt = LocalDateTime.now();
        pet.setDeletedAt(deletedAt);

        if (pet.getPetRecords() != null) {
            pet.getPetRecords().forEach(petRecord -> petRecord.setDeletedAt(deletedAt));
        }
        petRepository.save(pet);
    }

    /**
     * Restores a soft-deleted pet by setting its deletedAt timestamp to null.
     *
     * @param id the ID of the pet to be restored
     * @throws EntityNotFoundException if the pet with the specified ID is not found
     */
    @Transactional
    public void restorePet(@NotNull @Positive Long id) {
        Pet pet = petRepository.findByIdIncludingDeleted(id)
                .orElseThrow(() -> new EntityNotFoundException(PetMessages.PET_NOT_FOUND + id));

        if (pet.getDeletedAt() == null) {
            throw new IllegalStateException("Pet with id " + id + " is not deleted");
        }
        pet.setDeletedAt(null);
        petRepository.save(pet);
    }

    /**
     * Get all pet records by pet ID.
     *
     * @param id The ID of the pet.
     * @return A list of pet records.
     */
    @Cacheable(cacheNames = "record", key = "#id")
    public List<PetRecordDTO> getAllPetRecordsByPetId(@NotNull @Positive Long id) {
        Pet pet = findActivePetEntityById(id);
        return pet.getPetRecords().stream().map(PetRecordMapper.INSTANCE::toDTO).toList();
    }

    /**
     * Add a pet record.
     *
     * @param id                 The ID of the pet.
     * @param petRecordCreateDTO The pet record to add.
     * @return The pet record.
     * @throws EntityNotFoundException if the pet with the specified ID is not found.
     */
    @CacheEvict(cacheNames = "record", key = "#id")
    @Transactional
    public PetRecordDTO addPetRecord(@NotNull @Positive Long id, @Valid PetRecordCreateDTO petRecordCreateDTO) {
        Pet pet = findActivePetEntityById(id);

        PetRecord petRecord = PetRecordMapper.INSTANCE.toModel(petRecordCreateDTO);

        petRecord.setCreatedAt(LocalDateTime.now());
        petRecord.setPet(pet);

        petRecord = petRecordRepository.save(petRecord);
        return PetRecordMapper.INSTANCE.toDTO(petRecord);
    }

    /**
     * Retrieves all soft-deleted pets.
     *
     * @return a list of soft-deleted pet DTOs
     */
    public List<PetDTO> findAllDeletedPets() {
        List<Pet> pets = petRepository.findAllDeleted();
        return pets.stream().map(PetMapper.INSTANCE::toDTO).toList();
    }

    /**
     * Retrieves a soft-deleted pet by ID.
     *
     * @param id The ID of the pet.
     * @return The pet DTO.
     * @throws EntityNotFoundException if the pet with the specified ID is not found or not deleted.
     */
    public PetDTO findDeletedPetById(@NotNull @Positive Long id) {
        Pet pet = findDeletedPetEntityById(id);
        return PetMapper.INSTANCE.toDTO(pet);
    }

    public List<PetRecordDTO> findDeletedPetRecordEntityByPetId(@NotNull @Positive Long petId) {
        findDeletedPetEntityById(petId);
        List<PetRecord> deletedPetRecords = petRecordRepository.findAllByPet_IdAndDeletedAtIsNotNull(petId);

        return deletedPetRecords.stream()
                .map(PetRecordMapper.INSTANCE::toDTO)
                .toList();
    }

    /**
     * Removes favorites if the pet is adopted.
     *
     * @param petUpdateDTO The pet update DTO.
     * @param pet          The pet.
     */
    private void removeFavoritesIfAdopted(PetUpdateDTO petUpdateDTO, Pet pet) {
        if (Boolean.TRUE.equals(petUpdateDTO.getIsAdopted())) {
            List<Favorite> favorites = favoriteRepository.findByPet(pet);

            if (!favorites.isEmpty()) {
                favoriteRepository.deleteAll(favorites);
            }
        }
    }

    /**
     * Returns an active Pet entity.
     * To be used only in internal operations.
     *
     * @param id The ID of the pet.
     * @return The Pet entity.
     * @throws EntityNotFoundException if the pet with the specified ID is not found.
     */
    public Pet findActivePetEntityById(Long id) {
        return petRepository.findActiveById(id)
                .orElseThrow(() -> new EntityNotFoundException(PetMessages.PET_NOT_FOUND + id));
    }

    public Pet findDeletedPetEntityById(Long id) {
        return petRepository.findDeletedById(id)
                .orElseThrow(() -> new EntityNotFoundException(PetMessages.DELETED_PET_NOT_FOUND + id));
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
