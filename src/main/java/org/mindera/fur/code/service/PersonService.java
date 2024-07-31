package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.Person.PersonCreationDTO;
import org.mindera.fur.code.dto.Person.PersonDTO;
import org.mindera.fur.code.mapper.PersonMapper;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private PersonMapper personMapper;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonDTO createPerson(PersonCreationDTO personCreationDTO) {
        Person person = personMapper.INSTANCE.toModel(personCreationDTO);
        personRepository.save(person);
        return personMapper.INSTANCE.toDTO(person);
    }

    public List<PersonDTO> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        return personMapper.INSTANCE.toDTO(persons);
    }

    public PersonDTO getPersonById(Long id) {
        Person person = personRepository.findById(id).get();
        return personMapper.INSTANCE.toDTO(person);
    }

    public PersonDTO updatePerson(Long id, PersonDTO personDTO) {
        Person person = personRepository.findById(id).get();
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
        Person person = personRepository.findById(id).get();
        personRepository.delete(person);
    }

    public void deleteAllPersons() {
        personRepository.deleteAll();
    }
}
