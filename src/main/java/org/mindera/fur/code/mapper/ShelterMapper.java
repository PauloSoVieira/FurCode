package org.mindera.fur.code.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.dto.shelter.ShelterThemeDTO;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.ShelterTheme;

import java.util.List;

@Mapper
public interface ShelterMapper {

    ShelterMapper INSTANCE = Mappers.getMapper(ShelterMapper.class);

    ShelterDTO toDto(Shelter shelter);

    Shelter toModel(ShelterDTO shelterDTO);

    Shelter toModel(ShelterCreationDTO shelterCreationDTO);

    List<ShelterDTO> toDto(List<Shelter> shelter);

    List<Shelter> toModel(List<ShelterDTO> shelterDTO);

    ShelterThemeDTO toDto(ShelterTheme shelterTheme);

    ShelterTheme toModel(ShelterThemeDTO shelterThemeDTO);
}
