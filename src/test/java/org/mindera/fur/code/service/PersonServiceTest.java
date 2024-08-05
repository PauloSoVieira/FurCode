package org.mindera.fur.code.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.mapper.PersonMapper;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.repository.PersonRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;


    @Nested
    class getPerson {
        @Test
        void createPerson() {
            PersonCreationDTO personCreationDTO = new PersonCreationDTO();
            personCreationDTO.setFirstName("John");

            Person person = PersonMapper.INSTANCE.toModel(personCreationDTO);
            personRepository.save(person);

            verify(personRepository, times(1)).save(person);
        }


        @Test
        void getPersonById() {
            Person person = new Person();
            person.setId(1L);
            when(personRepository.findById(1L)).thenReturn(Optional.of(person));

            PersonMapper.INSTANCE.toDTO(person);

            personService.getPersonById(person.getId());

            verify(personRepository, times(1)).findById(1L);
            assertEquals(person.getId(), 1L);
        }

        @Test
        void getAllPersons() {
            Person person = new Person();
            person.setId(1L);
            when(personRepository.findAll()).thenReturn(List.of(person));

            personService.getAllPersons();

            verify(personRepository, times(1)).findAll();
        }

        @Test
        void updatePerson() {
            Person person = new Person();
            person.setId(1L);
            when(personRepository.findById(1L)).thenReturn(Optional.of(person));

            PersonDTO personUpdatedDTO = PersonMapper.INSTANCE.toDTO(person);
            personUpdatedDTO.setFirstName("John");
            personService.updatePerson(1L, personUpdatedDTO);

            verify(personRepository, times(1)).save(person);
        }

        @Test
        void deletePerson() {
            Person person = new Person();
            person.setId(1L);
            when(personRepository.findById(1L)).thenReturn(Optional.of(person));

            personService.deletePerson(1L);

            verify(personRepository, times(1)).delete(person);
        }
    }

}