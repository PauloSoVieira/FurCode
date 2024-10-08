package org.mindera.fur.code.mapper;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.model.Person;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
@Schema(description = "The person mapper")
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    @Mapping(source = "shelterIds", target = "shelterIds")
    PersonDTO toDTO(Person person);

    @Mapping(source = "shelterIds", target = "shelterIds")
    Person toModel(PersonDTO personDTO);

    @Mapping(target = "shelterIds", ignore = true)
    Person toModel(PersonCreationDTO personCreationDTO);

    List<Person> toModel(List<PersonDTO> personDTOS);

    List<PersonDTO> toDTO(List<Person> persons);

    default Set<Long> map(Set<Long> value) {
        return value;
    }
}