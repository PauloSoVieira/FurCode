package org.mindera.fur.code.mapper.shelter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.shelter.ShelterUpdateDTO;
import org.mindera.fur.code.model.Shelter;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShelterUpdateMapper {

    ShelterUpdateMapper INSTANCE = Mappers.getMapper(ShelterUpdateMapper.class);

    void updateShelterFromDto(ShelterUpdateDTO shelterUpdateDTO, @MappingTarget Shelter shelter);
}
