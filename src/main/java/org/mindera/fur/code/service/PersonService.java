package org.mindera.fur.code.service;


import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.exceptions.donation.person.*;
import org.mindera.fur.code.mapper.PersonMapper;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final ShelterService shelterService;
    private PersonMapper personMapper;

    @Autowired
    public PersonService(PersonRepository personRepository, ShelterService shelterService) {
        this.personRepository = personRepository;
        this.shelterService = shelterService;
    }

    private static void idValidation(Long id) {
        if (id == null) {
            throw new PersonsIdCannotBeNullException();
        }
        if (id <= 0) {
            throw new PersonsIdCannotBeLowerOrEqualZero();
        }
    }

    private static void personValidation(PersonCreationDTO personCreationDTO) {
        if (personCreationDTO.getFirstName() == null) {
            throw new PersonsNameCannotBeNull();
        }
        if (personCreationDTO.getFirstName().equals(" ")) {
            throw new PersonsNameCannotBeEmpty();
        }
        if (personCreationDTO.getLastName() == null) {
            throw new PersonsLastNameCannotBeNull();
        }
        if (personCreationDTO.getLastName().equals(" ")) {
            throw new PersonsLastNameCannotBeEmpty();
        }
        if (personCreationDTO.getEmail() == null) {
            throw new PersonsEmailCannotBeNull();
        }
        if (personCreationDTO.getEmail().equals(" ")) {
            throw new PersonsEmailCannotBeEmpty();
        }
        if (personCreationDTO.getPassword() == null) {
            throw new PersonsPasswordCannotBeNull();
        }
        if (personCreationDTO.getPassword().equals(" ")) {
            throw new PersonsPasswordCannotBeEmpty();
        }
        if (personCreationDTO.getPostalCode() == null) {
            throw new PersonsPostalCodeCannotBeNull();
        }
        if (personCreationDTO.getPostalCode() <= 0) {
            throw new PersonsPostalCodeCannotBeZero();
        }
        if (personCreationDTO.getAddress1() == null) {
            throw new PersonsAddressCannotBeNull();
        }
        if (personCreationDTO.getAddress1().equals(" ")) {
            throw new PersonsAddressCannotBeEmpty();
        }
    }

    public PersonDTO createPerson(PersonCreationDTO personCreationDTO) {
        personValidation(personCreationDTO);

        if (personRepository.findByEmail(personCreationDTO.getEmail()) != null) {
            throw new PersonsEmailCannotBeEmpty("Need to change this exception");
        }

        Person person = personMapper.INSTANCE.toModel(personCreationDTO);
        String encryptedPassword = new BCryptPasswordEncoder().encode(personCreationDTO.getPassword());
        person.setPassword(encryptedPassword); //TODO verificar se está sendo criado o não

        personRepository.save(person);
        return personMapper.INSTANCE.toDTO(person);
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
