package org.mindera.fur.code.mapper.pet;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.pet.PetCreateDTO;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.mindera.fur.code.model.pet.Pet;

import java.util.List;

/**
 * A mapper class for mapping pets.
 */
@Mapper(uses = PetRecordMapper.class)
public interface PetMapper {

    PetMapper INSTANCE = Mappers.getMapper(PetMapper.class);

    @Mapping(source = "petType.id", target = "petTypeId")
    @Mapping(source = "shelter.id", target = "shelterId")
    PetDTO toDTO(Pet pet);

    @Mapping(source = "petTypeId", target = "petType.id")
    @Mapping(source = "shelterId", target = "shelter.id")
    Pet toModel(PetDTO dto);

    @Mapping(source = "petTypeId", target = "petType.id")
    @Mapping(source = "shelterId", target = "shelter.id")
    Pet toModel(PetCreateDTO dto);

    @Mapping(source = "petType.id", target = "petTypeId")
    @Mapping(source = "shelter.id", target = "shelterId")
    List<PetDTO> toDto(List<Pet> pet);
}
