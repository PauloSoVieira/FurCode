package org.mindera.fur.code.mapper;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.model.Person;

import java.util.List;

@Mapper(componentModel = "spring")
@Schema(description = "The person mapper")
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonDTO toDTO(Person person);

    Person toModel(PersonDTO personDTO);

    Person toModel(PersonCreationDTO personCreationDTO);

    List<Person> toModel(List<PersonDTO> personDTOS);

    List<PersonDTO> toDTO(List<Person> personCreationDTOS);
}