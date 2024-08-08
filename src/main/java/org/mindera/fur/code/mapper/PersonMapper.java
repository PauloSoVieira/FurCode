package org.mindera.fur.code.mapper;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.model.Person;

import java.util.List;

@Mapper
@Schema(description = "The person mapper")
public interface PersonMapper {

    /**
     * Singleton instance of the mapper
     */
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    /**
     * Converts a Person to a PersonDTO
     **/
    PersonDTO toDTO(Person person);

    /**
     * Converts a PersonDTO to a Person
     **/
    Person toModel(PersonDTO personDTO);


    /**
     * Converts a PersonCreationDTO to a Person
     **/
    Person toModel(PersonCreationDTO personCreationDTO);

    /**
     * Converts a PersonDTO to a Person
     **/
    List<Person> toModel(List<PersonDTO> personDTOS);

    /**
     * Converts a Person to a PersonDTO
     **/
    List<PersonDTO> toDTO(List<Person> personCreationDTOS);
}
