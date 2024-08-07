package org.mindera.fur.code.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.shelterPersonRoles.ShelterPersonRolesCreationDTO;
import org.mindera.fur.code.dto.shelterPersonRoles.ShelterPersonRolesDTO;
import org.mindera.fur.code.model.ShelterPersonRoles;

import java.util.List;

@Mapper
public interface ShelterPersonRolesMapper {
    ShelterPersonRolesMapper INSTANCE = Mappers.getMapper(ShelterPersonRolesMapper.class);

    @Mapping(target = "personId", source = "person.id")
    @Mapping(target = "shelterId", source = "shelter.id")
    ShelterPersonRolesDTO toDto(ShelterPersonRoles shelterPersonRoles);


    ShelterPersonRoles toModel(ShelterPersonRolesDTO shelterPersonRolesDTO);


    ShelterPersonRolesCreationDTO toDTO(ShelterPersonRoles shelterPersonRoles);

    @Mapping(target = "person.id", source = "personId")
    @Mapping(target = "shelter.id", source = "shelterId")
    ShelterPersonRoles toModel(ShelterPersonRolesCreationDTO shelterPersonRolesCreationDTO);

    List<ShelterPersonRolesDTO> toDto(List<ShelterPersonRoles> shelterPersonRolesList);

    List<ShelterPersonRoles> toModel(List<ShelterPersonRolesDTO> shelterPersonRolesDTOList);
}