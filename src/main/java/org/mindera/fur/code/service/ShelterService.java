package org.mindera.fur.code.service;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.mindera.fur.code.dto.donation.DonationDTO;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.dto.shelter.ShelterUpdateDTO;
import org.mindera.fur.code.mapper.shelter.ShelterMapper;
import org.mindera.fur.code.mapper.shelter.ShelterUpdateMapper;
import org.mindera.fur.code.messages.shelter.ShelterMessages;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.pet.Pet;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.mindera.fur.code.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Service class for handling Shelters.
 */
@Schema(description = "The shelter service")
@Validated
@Service
public class ShelterService {

    private final PetService petService;
    private final DonationService donationService;
    private final ShelterRepository shelterRepository;
    private final PersonRepository personRepository;
    private final PetRepository petRepository;

    /**
     * Constructor for the ShelterService.
     *
     * @param shelterRepository the shelter repository
     * @param personRepository  the person repository
     * @param petRepository     the pet repository
     * @param petService        the pet service
     * @param donationService   the donation service
     */
    @Autowired
    public ShelterService(
            ShelterRepository shelterRepository,
            PersonRepository personRepository,
            PetRepository petRepository,
            PetService petService,
            DonationService donationService
    ) {
        this.shelterRepository = shelterRepository;
        this.personRepository = personRepository;
        this.petRepository = petRepository;
        this.petService = petService;
        this.donationService = donationService;
    }

    /**
     * Get all active shelters.
     *
     * @return a list of all active shelters.
     */
//    @Cacheable(cacheNames = "shelters")
    public List<ShelterDTO> getAllShelters() {
        List<Shelter> shelters = shelterRepository.findAll();
        return shelters.stream().map(ShelterMapper.INSTANCE::toDTO).toList();
    }

    /**
     * Get an active shelter by ID.
     *
     * @param id the ID of the shelter.
     * @return The shelter.
     */
//    @Cacheable(cacheNames = "shelter", key = "#id")
    public ShelterDTO getShelterById(@NotNull @Positive Long id) {
        Shelter shelter = findShelterEntityById(id);
        return ShelterMapper.INSTANCE.toDTO(shelter);
    }

    /**
     * Creates a new shelter.
     *
     * @param shelterCreationDTO The DTO containing shelter creation data.
     * @return The created shelter.
     */
//    @CachePut(cacheNames = "shelter", key = "#result.id")
//    @CacheEvict(cacheNames = "shelters", allEntries = true)
    @Transactional
    public ShelterDTO createShelter(@Valid ShelterCreationDTO shelterCreationDTO) {
        Shelter shelter = ShelterMapper.INSTANCE.toModel(shelterCreationDTO);
        shelter = shelterRepository.save(shelter);
        return ShelterMapper.INSTANCE.toDTO(shelter);
    }

    /**
     * Updates a shelter's details.
     *
     * @param id         the ID of the shelter.
     * @param shelterUpdateDTO The DTO containing updated pet information.
     * @throws EntityNotFoundException if the shelter with the specified ID is not found
     */
//    @CachePut(cacheNames = "shelter", key = "#id")
//    @CacheEvict(cacheNames = "shelters", allEntries = true)
    @Transactional
    public ShelterDTO updateShelter(@NotNull @Positive Long id, @Valid ShelterUpdateDTO shelterUpdateDTO) {
        Shelter shelter = findShelterEntityById(id);

        ShelterUpdateMapper.INSTANCE.updateShelterFromDto(shelterUpdateDTO, shelter);

        shelter = shelterRepository.save(shelter);
        return ShelterMapper.INSTANCE.toDTO(shelter);
    }

    /**
     * Soft deletes a shelter by setting its deletedAt timestamp.
     *
     * @param id the ID of the shelter to be soft deleted
     * @throws EntityNotFoundException if the shelter with the specified ID is not found
     */
//    @Caching(evict = {
//            @CacheEvict(cacheNames = "shelter", key = "#id"),
//            @CacheEvict(cacheNames = "shelters", allEntries = true)
//    })
    @Transactional
    public void deleteShelter(@NotNull @Positive Long id) {
        Shelter shelter = findShelterEntityById(id);
        shelterRepository.delete(shelter);
    }

    /**
     * Adds a pet to a shelter.
     *
     * @param shelterId the shelter id
     * @param petId     the pet id
     *
     * @throws EntityNotFoundException if the pet or shelter is not found
     */
    public void addPetToShelter(@NotNull @Positive Long shelterId, @NotNull @Positive Long petId) {
        Pet pet = petService.findActivePetEntityById(petId);
        Shelter shelter = findShelterEntityById(shelterId);

        pet.setShelter(shelter);
        petRepository.save(pet);
    }

    /**
     * Gets all active pets in a shelter.
     *
     * @param id the ID of the shelter.
     * @return a list of all active pets in the shelter.
     */
    public List<PetDTO> getAllPetsInShelter(@NotNull @Positive Long id) {
        return petService.findAllPets()
                .stream()
                .filter(pet -> pet.getShelterId().equals(id))
                .toList();
    }

    /**
     * Gets all donations by id.
     *
     * @param id the id
     * @return the list of donation dtos
     */
    public List<DonationDTO> getAllDonationsById(@NotNull @Positive Long id) {
        return donationService.getAllDonationsByShelterId(id);
    }

    /**
     * Returns an active Shelter entity.
     * To be used only in internal operations.
     *
     * @param id The ID of the shelter.
     * @return The Shelter entity.
     * @throws EntityNotFoundException if the shelter with the specified ID is not found.
     */
    public Shelter findShelterEntityById(@NotNull @Positive Long id) {
        return shelterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ShelterMessages.SHELTER_NOT_FOUND + id));
    }

   /* public List<Request> getAllRequests() {
        List<Person> persons = personRepository.findAll();
        return persons.stream()
                .filter(person -> person.getShelter().getId().equals(personId))
                .map(this::convertToPersonDTO)
                .collect(Collectors.toList());
    }

    public void requestAdoption(Long id, Long petId, Long personId) {
        Shelter shelter = shelterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shelter not found"));
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person not found"));
        pet.setShelter(shelter);
        person.setShelter(shelter);
        person.setPet(pet);
        shelter.getPets().add(pet);
        shelter.getPeople().add(person);
        petRepository.save(pet);
        personRepository.save(person);
        shelterRepository.save(shelter);
    }*/
}
