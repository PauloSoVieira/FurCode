package org.mindera.fur.code.service;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.transaction.Transactional;
import org.mindera.fur.code.dto.donation.DonationCreateDTO;
import org.mindera.fur.code.dto.donation.DonationDTO;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.dto.shelterPersonRoles.ShelterPersonRolesDTO;
import org.mindera.fur.code.exceptions.person.PersonException;
import org.mindera.fur.code.mapper.PersonMapper;
import org.mindera.fur.code.mapper.ShelterPersonRolesMapper;
import org.mindera.fur.code.messages.person.PersonMessages;
import org.mindera.fur.code.model.*;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.ShelterPersonRolesRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Schema(description = "The person service")
public class PersonService {
    private static final String COMPANY_EMAIL = "paulo.vieira@minderacodeacademy.com"; // Replace with your actual company email
    private final PersonRepository personRepository;
    private final ShelterRepository shelterRepository;
    private final ShelterService shelterService;
    private final DonationService donationService;
    private final ShelterPersonRolesRepository shelterPersonRolesRepository;
    private final Gmailer gmailer;
    private PersonMapper personMapper;
    private ShelterPersonRolesMapper shelterPersonRolesMapper;

    @Autowired
    public PersonService(PersonRepository personRepository, ShelterService shelterService,
                         ShelterPersonRolesRepository shelterPersonRolesRepository, DonationService donationService, ShelterRepository shelterRepository, Gmailer gmailer) throws Exception {
        this.personRepository = personRepository;
        this.shelterService = shelterService;
        this.shelterPersonRolesRepository = shelterPersonRolesRepository;
        this.shelterRepository = shelterRepository;
        this.gmailer = new Gmailer();
        this.donationService = donationService;
    }

    /**
     * Validates if the id is null or less than 0
     *
     * @param id The id to be validated
     * @throws PersonException if the id is null or less than 0
     */

    private static void idValidation(Long id) {
        if (id == null) {
            throw new PersonException(PersonMessages.ID_CANT_BE_NULL);
        }
        if (id <= 0) {
            throw new PersonException(PersonMessages.ID_CANT_BE_LOWER_OR_EQUAL_ZERO);
        }
    }

    /**
     * Validates the fields of the PersonCreationDTO object.
     * Throws a PersonException if any validation checks fail.
     *
     * @param personCreationDTO The DTO object containing person data to be validated.
     * @throws PersonException if any field fails validation.
     */

    private static void personValidation(PersonCreationDTO personCreationDTO) {

        if (personCreationDTO.getFirstName() == null) {
            throw new PersonException(PersonMessages.NAME_CANT_BE_NULL);
        }

        if (personCreationDTO.getFirstName().equals(" ")) {
            throw new PersonException(PersonMessages.NAME_CANT_BE_EMPTY);
        }
        if (personCreationDTO.getLastName() == null) {
            throw new PersonException(PersonMessages.LAST_NAME_CANT_BE_NULL);
        }
        if (personCreationDTO.getLastName().equals(" ")) {
            throw new PersonException(PersonMessages.LAST_NAME_CANT_BE_EMPTY);
        }
        if (personCreationDTO.getEmail() == null) {
            throw new PersonException(PersonMessages.EMAIL_CANT_BE_NULL);
        }
        if (personCreationDTO.getEmail().equals(" ")) {
            throw new PersonException(PersonMessages.EMAIL_CANT_BE_EMPTY);
        }
        if (personCreationDTO.getPassword() == null) {
            throw new PersonException(PersonMessages.PASSWORD_CANT_BE_NULL);
        }
        if (personCreationDTO.getPassword().equals(" ")) {
            throw new PersonException(PersonMessages.PASSWORD_CANT_BE_EMPTY);
        }
        if (personCreationDTO.getPostalCode() == null) {
            throw new PersonException(PersonMessages.POSTAL_CODE_CANT_BE_NULL);
        }
        if (personCreationDTO.getPostalCode() <= 0) {
            throw new PersonException(PersonMessages.POSTAL_CODE_CANT_BE_ZERO);
        }
        if (personCreationDTO.getAddress1() == null) {
            throw new PersonException(PersonMessages.ADDRESS_CANT_BE_NULL);
        }
        if (personCreationDTO.getAddress1().equals(" ")) {
            throw new PersonException(PersonMessages.ADDRESS_CANT_BE_EMPTY);
        }
    }

    /**
     * Creates a new person based on the provided PersonCreationDTO.
     *
     * <p>This method performs several validation checks on the provided PersonCreationDTO:
     * <ul>
     *   <li>Ensures that the first name, last name, email, password, address1, and address2
     *       are not null and that the password is not empty.</li>
     *   <li>Validates that the postal code is greater than 0 and the address1 is not empty.</li>
     *   <li>Ensures that the email is unique.</li>
     * </ul>
     * If any of these conditions are not met, an appropriate exception is thrown.
     *
     * <p>After successful validation, the person is mapped to a Person model object
     * and saved to the repository.
     *
     * @param personCreationDTO the PersonCreationDTO containing the person details
     * @return the saved Person object
     * @throws PersonException if any required fields are null or invalid
     * @throws PersonException if the email is already in use
     */
    public PersonDTO createPerson(PersonCreationDTO personCreationDTO) {
        personValidation(personCreationDTO);

        if (personRepository.findByEmail(personCreationDTO.getEmail()) != null) {
            throw new PersonException(PersonMessages.EMAIL_ALREADY_EXISTS);
        }

        Person person = personMapper.INSTANCE.toModel(personCreationDTO);
        person.setRole(Role.USER);

        String encryptedPassword = new BCryptPasswordEncoder().encode(personCreationDTO.getPassword());
        person.setPassword(encryptedPassword);

        Person savedPerson = personRepository.save(person);

        try {
            try {
                // Send welcome email to the user
                gmailer.sendMail(savedPerson.getEmail(), "Welcome to FurCode",
                        "Dear " + savedPerson.getFirstName() + ",\n\nWelcome to FurCode! We're excited to have you on board.");

                // Send notification to the company
                gmailer.sendMail(COMPANY_EMAIL, "New user registration",
                        "A new user has registered:\nName: " + savedPerson.getFirstName() + " " + savedPerson.getLastName() +
                                "\nEmail: " + savedPerson.getEmail());
            } catch (Exception e) {
                // Log the error, but don't prevent user creation if email sending fails
                System.err.println("Failed to send email: " + e.getMessage());
            }

            return personMapper.INSTANCE.toDTO(savedPerson);
        } catch (Exception e) {
            throw new PersonException(PersonMessages.FAILED_TO_CREATE_PERSON);
        }
    }

    /**
     * Adds a person to a shelter with an assigned role.
     *
     * @param personId  the ID of the person to be added to the shelter
     * @param shelterId the ID of the shelter to which the person is being added
     * @return ShelterPersonRolesDTO containing the details of the person, shelter, and assigned role
     * @throws PersonException if the person or shelter is not found
     * @apiNote This method assigns a person to a shelter and sets their role within the shelter.
     * The role is based on the person's current role.
     */

    @Transactional
    public ShelterPersonRolesDTO addPersonToShelter(Long personId, Long shelterId) {
        Person person = personRepository.findById(personId).orElseThrow(
                () -> new PersonException(PersonMessages.PERSON_NOT_FOUND)
        );
        Shelter shelter = shelterRepository.findById(shelterId).orElseThrow(
                () -> new PersonException(PersonMessages.SHELTER_NOT_FOUND)
        );

        ShelterPersonRoles shelterPersonRoles = new ShelterPersonRoles();
        shelterPersonRoles.setPerson(person);
        shelterPersonRoles.setShelter(shelter);
        shelterPersonRoles.setRole(person.getRole());

        shelterPersonRoles = shelterPersonRolesRepository.save(shelterPersonRoles);

        return shelterPersonRolesMapper.INSTANCE.toDto(shelterPersonRoles);
    }

    /**
     * Retrieves all persons from the repository.
     *
     * <p>This method fetches all person records from the repository, maps each one to a
     * PersonDTO, and collects them in an ArrayList which is then returned.
     *
     * @return an ArrayList of PersonDTO objects representing all persons
     */

    public List<PersonDTO> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        return personMapper.INSTANCE.toDTO(persons);
    }

    /**
     * Retrieves a person by its ID.
     *
     * <p>This method attempts to find a person record in the repository using the provided ID.
     * If the person is found, it is mapped to a PersonDTO and returned.
     * If the person is not found, a {@code PersonNotFoundException} is thrown.
     *
     * @param id the ID of the person to retrieve
     * @return the PersonDTO representing the person details
     * @throws PersonException if no person with the specified ID is found
     */

    public PersonDTO getPersonById(Long id) {
        idValidation(id);
        Person person = personRepository.findById(id).orElseThrow(
                () -> new PersonException(PersonMessages.PERSON_NOT_FOUND)
        );
        return personMapper.INSTANCE.toDTO(person);
    }

    /**
     * Updates a person based on the provided PersonDTO.
     *
     * <p>This method performs several validation checks on the provided PersonDTO:
     * <ul>
     *   <li>Ensures that the first name, last name, email, password, address1, and address2
     *       are not null and that the password is not empty.</li>
     *   <li>Validates that the postal code is greater than 0 and the address1 is not empty.</li>
     *   <li>Ensures that the email is unique.</li>
     * </ul>
     * If any of these conditions are not met, an appropriate exception is thrown.
     *
     * <p>After successful validation, the person is mapped to a Person model object
     * and saved to the repository.
     *
     * @param id        the ID of the person to update
     * @param personDTO the PersonDTO containing the person details to update
     * @return the updated Person object
     * @throws PersonException if any required fields are null or invalid
     * @throws PersonException if the email is already in use
     */

    public PersonDTO updatePerson(Long id, PersonDTO personDTO) {
        idValidation(id);
        Person person = personRepository.findById(id).orElseThrow(
                () -> new PersonException(PersonMessages.PERSON_NOT_FOUND)
        );
        Person updatedPerson = personMapper.INSTANCE.toModel(personDTO);
        person.setFirstName(updatedPerson.getFirstName());
        person.setLastName(updatedPerson.getLastName());
        person.setEmail(updatedPerson.getEmail());
        person.setPassword(updatedPerson.getPassword());
        person.setAddress1(updatedPerson.getAddress1());
        person.setAddress2(updatedPerson.getAddress2());
        person.setPostalCode(updatedPerson.getPostalCode());
        person.setCellPhone(updatedPerson.getCellPhone());
        personRepository.save(person);
        return personMapper.INSTANCE.toDTO(person);
    }

    /**
     * Deletes a person based on the provided ID.
     *
     * <p>This method attempts to find a person record in the repository using the provided ID.
     * If the person is found, it is deleted from the repository.
     *
     * @param id the ID of the person to delete
     * @throws PersonException if no person with the specified ID is found
     */

    public void deletePerson(Long id) {
        Person person = personRepository.findById(id).orElseThrow(
                () -> new PersonException(PersonMessages.PERSON_NOT_FOUND)
        );
        personRepository.delete(person);
    }

    /**
     * Deletes all persons from the repository.
     *
     * <p>This method fetches all person records from the repository, maps each one to a
     * PersonDTO, and collects them in an ArrayList which is then deleted from the repository.
     *
     * @throws PersonException if no person with the specified ID is found
     */

    public void deleteAllPersons() {
        personRepository.deleteAll();
    }

    /**
     * /**
     * Creates a new shelter and assigns the person as an admin of that shelter.
     *
     * @param id                 the ID of the person who is creating the shelter
     * @param shelterCreationDTO the data required to create the shelter
     * @return ShelterPersonRolesDTO containing the details of the created shelter and the person's assigned role
     * @throws PersonException if the person is not found
     * @apiNote This method creates a shelter and assigns the person as an admin of the newly created shelter.
     * The person's role is updated to MANAGER.
     */
    public ShelterPersonRolesDTO createShelter(Long id, ShelterCreationDTO shelterCreationDTO) {
        idValidation(id);
        Person person = personRepository.findById(id).orElseThrow(
                () -> new PersonException(PersonMessages.PERSON_NOT_FOUND)
        );
        person.setRole(Role.MANAGER);
        personRepository.save(person);
        ShelterDTO shelter = shelterService.createShelter(shelterCreationDTO);
        Long shelterId = shelter.getId();
        return addPersonToShelter(id, shelterId);
    }

    /**
     * Sets the role of a person based on the provided PersonDTO.
     *
     * <p>This method performs several validation checks on the provided PersonDTO:
     * <ul>
     *   <li>Ensures that the person ID and role are not null and that the person ID is greater than 0.</li>
     *   <li>Ensures that the role is not null and that it is a valid role.</li>
     * </ul>
     * If any of these conditions are not met, an appropriate exception is thrown.
     *
     * <p>After successful validation, the person is mapped to a Person model object
     * and saved to the repository.
     *
     * @param id   the ID of the person to update
     * @param role the role to set
     * @return the updated Person object
     * @throws PersonException if any required fields are null or invalid
     * @throws PersonException if the email is already in use
     */

    public PersonDTO setPersonRole(Long id, Role role) {
        idValidation(id);
        Person person = personRepository.findById(id).orElseThrow(
                () -> new PersonException(PersonMessages.PERSON_NOT_FOUND)
        );
        person.setRole(role);
        personRepository.save(person);
        return personMapper.INSTANCE.toDTO(person);
    }

    /**
     * Creates a new donation based on the provided DonationCreateDTO.
     *
     * <p>This method performs several validation checks on the provided DonationCreateDTO:
     * <ul>
     *   <li>Ensures that the donation ID, total amount, date, pet ID, and person ID
     *       are not null and that the total amount is greater than 0.</li>
     *   <li>Validates that the donation date is in the future.</li>
     *   <li>Ensures that the total donation amount is less than 999999.</li>
     * </ul>
     * If any of these conditions are not met, an appropriate exception is thrown.
     *
     * <p>After successful validation, the donation is mapped to a Donation model object
     * and saved to the repository.
     *
     * @param id                the ID of the person whose donations are to be retrieved
     * @param donationCreateDTO the DonationCreateDTO containing the donation details
     * @return the saved Donation object
     * @throws PersonException if any required fields are null or invalid
     * @throws PersonException if the email is already in use
     */

    public DonationDTO donate(Long id, DonationCreateDTO donationCreateDTO) {
        idValidation(id);
        personRepository.findById(id).orElseThrow(
                () -> new PersonException(PersonMessages.PERSON_NOT_FOUND)
        );
        return donationService.createDonation(donationCreateDTO);
    }

    /**
     * Retrieves all donations from the repository.
     *
     * <p>This method fetches all donation records from the repository, maps each one to a
     * DonationDTO, and collects them in an ArrayList which is then returned.
     *
     * @return an ArrayList of DonationDTO objects representing all donations
     */

    public List<DonationDTO> getAllDonationsById(Long id) {
        idValidation(id);
        personRepository.findById(id).orElseThrow(
                () -> new PersonException(PersonMessages.PERSON_NOT_FOUND)
        );
        return donationService.getAllDonationsByPersonId(id);
    }
}