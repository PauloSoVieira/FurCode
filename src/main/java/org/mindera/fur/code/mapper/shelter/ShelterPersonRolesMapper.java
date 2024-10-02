package org.mindera.fur.code.mapper.shelter;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.shelterPersonRoles.ShelterPersonRolesCreationDTO;
import org.mindera.fur.code.dto.shelterPersonRoles.ShelterPersonRolesDTO;
import org.mindera.fur.code.model.ShelterPersonRoles;

import java.util.List;

@Mapper
@Schema(description = "The shelter person roles mapper")
public interface ShelterPersonRolesMapper {

    /**
     * Singleton instance of the mapper
     */
    ShelterPersonRolesMapper INSTANCE = Mappers.getMapper(ShelterPersonRolesMapper.class);

    /**
     * Converts a ShelterPersonRoles to a ShelterPersonRolesDTO
     **/
    @Mapping(target = "personId", source = "person.id")
    @Mapping(target = "shelterId", source = "shelter.id")
    ShelterPersonRolesDTO toDto(ShelterPersonRoles shelterPersonRoles);

    /**
     * Converts a ShelterPersonRolesDTO to a ShelterPersonRoles
     **/
    ShelterPersonRoles toModel(ShelterPersonRolesDTO shelterPersonRolesDTO);

    /**
     * Converts a ShelterPersonRoles to a ShelterPersonRolesCreationDTO
     **/
    ShelterPersonRolesCreationDTO toDTO(ShelterPersonRoles shelterPersonRoles);

    /**
     * Converts a ShelterPersonRolesCreationDTO to a ShelterPersonRoles
     **/
    @Mapping(target = "person.id", source = "personId")
    @Mapping(target = "shelter.id", source = "shelterId")
    ShelterPersonRoles toModel(ShelterPersonRolesCreationDTO shelterPersonRolesCreationDTO);


    /**
     * Converts a List of ShelterPersonRoles to a List of ShelterPersonRolesDTO
     **/
    List<ShelterPersonRolesDTO> toDto(List<ShelterPersonRoles> shelterPersonRolesList);


    /**
     * Converts a List of ShelterPersonRolesDTO to a List of ShelterPersonRoles
     **/
    List<ShelterPersonRoles> toModel(List<ShelterPersonRolesDTO> shelterPersonRolesDTOList);
}