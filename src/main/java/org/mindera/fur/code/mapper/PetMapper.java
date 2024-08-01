package org.mindera.fur.code.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.pet.PetCreateDTO;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.mindera.fur.code.model.Pet;

@Mapper
public interface PetMapper {

    PetMapper INSTANCE = Mappers.getMapper(PetMapper.class);

    PetDTO toDTO(Pet pet);

    Pet toModel(PetDTO petDTO);

    Pet toModel(PetCreateDTO petCreateDTO);
}