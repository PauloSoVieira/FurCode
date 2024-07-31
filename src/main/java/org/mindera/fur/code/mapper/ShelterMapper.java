package org.mindera.fur.code.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.ShelterDTO.ShelterCreationDTO;
import org.mindera.fur.code.dto.ShelterDTO.ShelterDTO;
import org.mindera.fur.code.model.Shelter;

import java.util.List;

@Mapper
public interface ShelterMapper {

    ShelterMapper INSTANCE = Mappers.getMapper(ShelterMapper.class);

    ShelterDTO toDto(Shelter shelter);

    Shelter toModel(ShelterDTO shelterDTO);

    ShelterCreationDTO toDTO(Shelter shelter);

    Shelter toModel(ShelterCreationDTO shelterCreationDTO);

    List<ShelterDTO> toDto(List<Shelter> shelter);

    List<Shelter> toModel(List<ShelterDTO> shelterDTO);
}
