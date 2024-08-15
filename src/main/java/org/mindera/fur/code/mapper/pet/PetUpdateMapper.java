package org.mindera.fur.code.mapper.pet;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.pet.PetUpdateDTO;
import org.mindera.fur.code.model.pet.Pet;

/**
 * A mapper class for updating a pet.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PetUpdateMapper {

    PetUpdateMapper INSTANCE = Mappers.getMapper(PetUpdateMapper.class);

    void updatePetFromDto(PetUpdateDTO petUpdateDTO, @MappingTarget Pet pet);
}
