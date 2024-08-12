package org.mindera.fur.code.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.model.Shelter;

import java.util.List;

/**
 * Mapper class for the Shelter entity.
 */
@Mapper
public interface ShelterMapper {

    /**
     * Static instance of the mapper.
     */
    ShelterMapper INSTANCE = Mappers.getMapper(ShelterMapper.class);

    /**
     * Converts a Shelter object to a ShelterDTO object.
     *
     * @param shelter The Shelter object to convert.
     * @return The converted ShelterDTO object.
     */
    ShelterDTO toDto(Shelter shelter);

    /**
     * Converts a ShelterDTO object to a Shelter object.
     *
     * @param shelterDTO The ShelterDTO object to convert.
     * @return The converted Shelter object.
     */
    Shelter toModel(ShelterDTO shelterDTO);

    /**
     * Converts a ShelterCreationDTO object to a Shelter object.
     *
     * @param shelterCreationDTO The ShelterCreationDTO object to convert.
     * @return The converted Shelter object.
     */
    Shelter toModel(ShelterCreationDTO shelterCreationDTO);

    /**
     * Converts a list of ShelterDTO objects to a list of Shelter objects.
     *
     * @param shelter The list of ShelterDTO objects to convert.
     * @return The converted list of Shelter objects.
     */
    List<ShelterDTO> toDto(List<Shelter> shelter);

    /**
     * Converts a list of Shelter objects to a list of ShelterDTO objects.
     *
     * @param shelterDTO The list of Shelter objects to convert.
     * @return The converted list of ShelterDTO objects.
     */
    List<Shelter> toModel(List<ShelterDTO> shelterDTO);
}
