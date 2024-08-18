package org.mindera.fur.code.service;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.dto.donation.DonationDTO;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.exceptions.person.PersonException;
import org.mindera.fur.code.mapper.ShelterMapper;
import org.mindera.fur.code.mapper.pet.PetMapper;
import org.mindera.fur.code.messages.shelter.ShelterMessages;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.pet.Pet;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.mindera.fur.code.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for handling Shelters.
 */
@Service
@Schema(description = "The shelter service")
public class ShelterService {

    private final PetService petService;
    private final DonationService donationService;
    private final ShelterRepository shelterRepository;
    private final PersonRepository personRepository;
    private final PetRepository petRepository;

    private ShelterMapper shelterMapper;
    private PetMapper petMapper;

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
    public ShelterService(ShelterRepository shelterRepository,
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
     * Validates the id.
     *
     * @param id the id
     */
    private static void idValidation(Long id) {
        if (id == null) {
            throw new PersonException(ShelterMessages.ID_CANT_BE_NULL);
        }
        if (id <= 0) {
            throw new PersonException(ShelterMessages.ID_CANT_BE_LOWER_OR_EQUAL_ZERO);
        }
    }

    /**
     * Validates the shelter creation dto.
     *
     * @param shelterCreationDTO the shelter creation dto
     */
    private static void shelterValidation(ShelterCreationDTO shelterCreationDTO) {

        String emailRegex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-zA-Z]{2,})$";

        if (!shelterCreationDTO.getEmail().matches(emailRegex)) {
            throw new PersonException(ShelterMessages.EMAIL_FORMAT_INVALID);
        }

        if (shelterCreationDTO.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ShelterMessages.NAME_CANT_BE_NULL);
        }
        if (shelterCreationDTO.getName().equals(" ")) {
            throw new PersonException(ShelterMessages.NAME_CANT_BE_EMPTY);
        }
        if (shelterCreationDTO.getVat() == null) {
            throw new PersonException(ShelterMessages.VAT_CANT_BE_NULL);
        }
        if (shelterCreationDTO.getVat() <= 0) {
            throw new PersonException(ShelterMessages.VAT_CANT_BE_ZERO_OR_LOWER);
        }
        if (shelterCreationDTO.getEmail() == null) {
            throw new PersonException(ShelterMessages.EMAIL_CANT_BE_NULL);
        }
        if (shelterCreationDTO.getEmail().equals(" ")) {
            throw new PersonException(ShelterMessages.EMAIL_CANT_BE_EMPTY);
        }
        if (shelterCreationDTO.getPostalCode() == null) {
            throw new PersonException(ShelterMessages.POSTAL_CODE_CANT_BE_NULL);
        }
        if (shelterCreationDTO.getPostalCode().equals(" ")) {
            throw new PersonException(ShelterMessages.POSTAL_CODE_CANT_BE_EMPTY);
        }
        if (shelterCreationDTO.getAddress1() == null) {
            throw new PersonException(ShelterMessages.ADDRESS_CANT_BE_NULL);
        }
        if (shelterCreationDTO.getAddress1().equals(" ")) {
            throw new PersonException(ShelterMessages.ADDRESS_CANT_BE_EMPTY);
        }
        if (shelterCreationDTO.getPhone() == null) {
            throw new PersonException(ShelterMessages.PHONE_CANT_BE_NULL);
        }
        if (shelterCreationDTO.getPhone() <= 0) {
            throw new PersonException(ShelterMessages.PHONE_CANT_BE_ZERO_OR_LOWER);
        }
        if (shelterCreationDTO.getSize() == null) {
            throw new PersonException(ShelterMessages.SIZE_CANT_BE_NULL);
        }
        if (shelterCreationDTO.getSize() <= 0) {
            throw new PersonException(ShelterMessages.SIZE_CANT_BE_ZERO_OR_LOWER);
        }
        if (shelterCreationDTO.getSize() > 1000) {
            throw new PersonException(ShelterMessages.SIZE_CANT_BE_HIGHER_THAN_1000);
        }
        if (shelterCreationDTO.getIsActive() == null) {
            throw new PersonException(ShelterMessages.ISACTIVE_CANT_BE_NULL);
        }
        if (shelterCreationDTO.getCreationDate() == null) {
            throw new PersonException(ShelterMessages.CREATION_DATE_CANT_BE_NULL);
        }

        if (shelterCreationDTO.getCreationDate().isAfter(LocalDate.now())) {
            throw new PersonException(ShelterMessages.CREATION_DATE_CANT_BE_IN_FUTURE);
        }
    }

    /**
     * Gets all shelters.
     *
     * @return the list of shelter dtos
     */
    @Cacheable(cacheNames = "shelters")
    public List<ShelterDTO> getAllShelters() {
        List<Shelter> shelters = shelterRepository.findAll();
        return shelterMapper.INSTANCE.toDto(shelters);
    }

    /**
     * Gets a shelter by id.
     *
     * @param id the id
     * @return the shelter dto
     */
    public ShelterDTO getShelterById(Long id) {
        idValidation(id);
        Shelter shelter = shelterRepository.findById(id).orElseThrow();
        return shelterMapper.INSTANCE.toDto(shelter);
    }

    /**
     * Creates a shelter.
     *
     * @param shelterCreationDTO the shelter creation dto
     * @return the shelter dto
     */
    @CacheEvict(cacheNames = "shelters", allEntries = true)
    public ShelterDTO createShelter(ShelterCreationDTO shelterCreationDTO) {
        shelterValidation(shelterCreationDTO);
        Shelter shelter = shelterMapper.INSTANCE.toModel(shelterCreationDTO);
        shelterRepository.save(shelter);
        return ShelterMapper.INSTANCE.toDto(shelter);
    }

    /**
     * Deletes a shelter by id.
     *
     * @param id the id
     * @return the shelter dto
     */
    @CacheEvict(cacheNames = "shelters", allEntries = true)
    public ShelterDTO deleteShelter(Long id) {
        idValidation(id);
        Shelter shelter = shelterRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Error"));
        shelterRepository.delete(shelter);
        return ShelterMapper.INSTANCE.toDto(shelter);
    }

    /**
     * Updates a shelter.
     *
     * @param id         the id
     * @param shelterDTO the shelter dto
     * @return the shelter dto
     */
    @CacheEvict(cacheNames = "shelters", allEntries = true)
    public ShelterDTO updateShelter(Long id, ShelterDTO shelterDTO) {
        idValidation(id);
        Shelter shelter = shelterRepository.findById(id).orElseThrow();
        Shelter updateShelter = shelterMapper.INSTANCE.toModel(shelterDTO);

        if (shelterDTO.getName() != null) {
            shelter.setName(shelterDTO.getName());
        }
        if (shelterDTO.getEmail() != null) {
            shelter.setEmail(shelterDTO.getEmail());
        }
        if (shelterDTO.getAddress1() != null) {
            shelter.setAddress1(shelterDTO.getAddress1());
        }
        if (shelterDTO.getAddress2() != null) {
            shelter.setAddress2(shelterDTO.getAddress2());
        }
        if (shelterDTO.getPostalCode() != null) {
            shelter.setPostalCode(shelterDTO.getPostalCode());
        }
        if (shelterDTO.getPhone() != null) {
            shelter.setPhone(shelterDTO.getPhone());
        }
        if (shelterDTO.getSize() != null) {
            shelter.setSize(shelterDTO.getSize());
        }
        if (shelterDTO.getIsActive() != null) {
            shelter.setIsActive(shelterDTO.getIsActive());
        }
        shelterRepository.save(shelter);
        return shelterMapper.INSTANCE.toDto(shelter);
    }

    /**
     * Deletes all shelters.
     */
    public void deleteAllShelters() {
        shelterRepository.deleteAll();
    }

    /**
     * Adds a pet to a shelter.
     *
     * @param shelterId the shelter id
     * @param petId     the pet id
     */
    public void addPetToShelter(Long shelterId, Long petId) {
        idValidation(shelterId);
        Pet pet = petRepository.findById(petId)
                .orElseThrow();
        pet.setShelter(shelterRepository.findById(shelterId)
                .orElseThrow(() -> new IllegalArgumentException(ShelterMessages.SHELTER_NOT_FOUND)));
        petRepository.save(pet);
    }

    /**
     * Gets all pets in a shelter.
     *
     * @param id the id
     * @return the list of pet dtos
     */
    public List<PetDTO> getAllPetsInShelter(Long id) {
        idValidation(id);
        return petService.findAllPets()
                .stream()
                .filter(pet -> pet
                        .getShelterId()
                        .equals(id))
                .toList();
    }

    /**
     * Gets all donations by id.
     *
     * @param id the id
     * @return the list of donation dtos
     */
    public List<DonationDTO> getAllDonationsById(Long id) {
        idValidation(id);
        return donationService.getAllDonationsByShelterId(id);
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
