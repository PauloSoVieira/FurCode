package org.mindera.fur.code.service;


import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.dto.shelterPersonRoles.ShelterPersonRolesCreationDTO;
import org.mindera.fur.code.dto.shelterPersonRoles.ShelterPersonRolesDTO;
import org.mindera.fur.code.exceptions.person.PersonException;
import org.mindera.fur.code.mapper.PersonMapper;
import org.mindera.fur.code.mapper.ShelterPersonRolesMapper;
import org.mindera.fur.code.messages.PersonsMessages;
import org.mindera.fur.code.model.Gmailer;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.ShelterPersonRoles;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.ShelterPersonRolesRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final ShelterRepository shelterRepository;
    private final ShelterService shelterService;
    private final ShelterPersonRolesRepository shelterPersonRolesRepository;
    private PersonMapper personMapper;
    private final Gmailer gmailer;

    private static final String COMPANY_EMAIL = "paulo.vieira@minderacodeacademy.com"; // Replace with your actual company email


    private ShelterPersonRolesMapper shelterPersonRolesMapper;

    @Autowired
    public PersonService(PersonRepository personRepository, ShelterService shelterService,
                         ShelterPersonRolesRepository shelterPersonRolesRepository, ShelterRepository shelterRepository, Gmailer gmailer) throws Exception {
        this.personRepository = personRepository;
        this.shelterService = shelterService;
        this.shelterPersonRolesRepository = shelterPersonRolesRepository;
        this.shelterRepository = shelterRepository;
        this.gmailer = new Gmailer();
    }

    private static void idValidation(Long id) {
        if (id == null) {
            throw new PersonException(PersonsMessages.ID_CANT_BE_NULL);
        }
        if (id <= 0) {
            throw new PersonException(PersonsMessages.ID_CANT_BE_LOWER_OR_EQUAL_ZERO);
        }
    }

    private static void personValidation(PersonCreationDTO personCreationDTO) {

        if (personCreationDTO.getFirstName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PersonsMessages.NAME_CANT_BE_NULL);
        }

        if (personCreationDTO.getFirstName().equals(" ")) {
            throw new PersonException(PersonsMessages.NAME_CANT_BE_EMPTY);
        }
        if (personCreationDTO.getLastName() == null) {
            throw new PersonException(PersonsMessages.LAST_NAME_CANT_BE_NULL);
        }
        if (personCreationDTO.getLastName().equals(" ")) {
            throw new PersonException(PersonsMessages.LAST_NAME_CANT_BE_EMPTY);
        }
        if (personCreationDTO.getEmail() == null) {
            throw new PersonException(PersonsMessages.EMAIL_CANT_BE_NULL);
        }
        if (personCreationDTO.getEmail().equals(" ")) {
            throw new PersonException(PersonsMessages.EMAIL_CANT_BE_EMPTY);
        }
        if (personCreationDTO.getPassword() == null) {
            throw new PersonException(PersonsMessages.PASSWORD_CANT_BE_NULL);
        }
        if (personCreationDTO.getPassword().equals(" ")) {
            throw new PersonException(PersonsMessages.PASSWORD_CANT_BE_EMPTY);
        }
        if (personCreationDTO.getPostalCode() == null) {
            throw new PersonException(PersonsMessages.POSTAL_CODE_CANT_BE_NULL);
        }
        if (personCreationDTO.getPostalCode() <= 0) {
            throw new PersonException(PersonsMessages.POSTAL_CODE_CANT_BE_ZERO);
        }
        if (personCreationDTO.getAddress1() == null) {
            throw new PersonException(PersonsMessages.ADDRESS_CANT_BE_NULL);
        }
        if (personCreationDTO.getAddress1().equals(" ")) {
            throw new PersonException(PersonsMessages.ADDRESS_CANT_BE_EMPTY);
        }
    }

    public PersonDTO createPerson(PersonCreationDTO personCreationDTO) {
        personValidation(personCreationDTO);

        if (personRepository.findByEmail(personCreationDTO.getEmail()) != null) {
            throw new PersonException(PersonsMessages.EMAIL_ALREADY_EXISTS);
        }

        Person person = personMapper.INSTANCE.toModel(personCreationDTO);
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
            throw new PersonException(PersonsMessages.FAILED_TO_CREATE_PERSON);
        }
    }
    public ShelterPersonRolesDTO addPersonToShelter(ShelterPersonRolesCreationDTO shelterPersonRolesCreationDTO) {

        Person person = personRepository.findById(shelterPersonRolesCreationDTO.getPersonId()).orElseThrow(PersonException::new);
        Shelter shelter = shelterRepository.findById(shelterPersonRolesCreationDTO.getShelterId()).orElseThrow(PersonException::new);  //TODO change this exception

        ShelterPersonRoles shelterPersonRoles = new ShelterPersonRoles();

        shelterPersonRoles.setShelter(shelter);
        shelterPersonRoles.setPerson(person);
        shelterPersonRoles.setRole(shelterPersonRolesCreationDTO.getRole());
        shelterPersonRolesRepository.save(shelterPersonRoles);

        return shelterPersonRolesMapper.INSTANCE.toDto(shelterPersonRoles);
    }

    public List<PersonDTO> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        return personMapper.INSTANCE.toDTO(persons);
    }

    public PersonDTO getPersonById(Long id) {
        idValidation(id);
        Person person = personRepository.findById(id).get(); //TODO verificar melhor forma para este get
        return personMapper.INSTANCE.toDTO(person);
    }

    public PersonDTO updatePerson(Long id, PersonDTO personDTO) {
        idValidation(id);
        Person person = personRepository.findById(id).get(); //TODO verificar melhor forma para este get
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

    public void deletePerson(Long id) {
        idValidation(id);
        Person person = personRepository.findById(id).get();
        personRepository.delete(person);
    }

    public void deleteAllPersons() {
        personRepository.deleteAll();
    }

    public ShelterDTO createShelter(ShelterCreationDTO shelterCreationDTO) {
        return shelterService.createShelter(shelterCreationDTO);
    }

    public PersonDTO setPersonRole(Long id, PersonDTO personDTO) {
        Person person = personRepository.findById(id).orElseThrow();
        Person personWithNewRole = personMapper.INSTANCE.toModel(personDTO);
        person.setRole(personWithNewRole.getRole());
        personRepository.save(person);
        return personMapper.INSTANCE.toDTO(person);
    }

}
