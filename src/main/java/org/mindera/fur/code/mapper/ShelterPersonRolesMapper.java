package org.mindera.fur.code.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.shelterPersonRoles.ShelterPersonRolesCreationDTO;
import org.mindera.fur.code.dto.shelterPersonRoles.ShelterPersonRolesDTO;
import org.mindera.fur.code.model.ShelterPersonRoles;

import java.util.List;

@Mapper
public interface ShelterPersonRolesMapper {
    ShelterPersonRolesMapper INSTANCE = Mappers.getMapper(ShelterPersonRolesMapper.class);

    ShelterPersonRolesDTO toDto(ShelterPersonRoles shelterPersonRoles);

    ShelterPersonRoles toModel(ShelterPersonRolesDTO shelterPersonRolesDTO);

    ShelterPersonRolesCreationDTO toDTO(ShelterPersonRoles shelterPersonRoles);

    ShelterPersonRoles toModel(ShelterPersonRolesCreationDTO shelterPersonRolesCreationDTO);

    List<ShelterPersonRolesDTO> toDto(List<ShelterPersonRoles> shelterPersonRolesList);

    List<ShelterPersonRoles> toModel(List<ShelterPersonRolesDTO> shelterPersonRolesDTOList);
}
