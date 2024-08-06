package org.mindera.fur.code.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.model.AdoptionRequest;

import java.util.List;

@Mapper
public interface AdoptionRequestMapper {

    AdoptionRequestMapper INSTANCE = Mappers.getMapper(AdoptionRequestMapper.class);

    AdoptionRequestDTO toDTO(AdoptionRequest adoptionRequest);

    AdoptionRequest toModel(AdoptionRequestDTO adoptionRequestDTO);

    AdoptionRequest toModel(AdoptionRequestCreationDTO adoptionRequestCreationDTO);

    List<AdoptionRequestDTO> toDTO(List<AdoptionRequest> adoptionRequest);

    List<AdoptionRequest> toModel(List<AdoptionRequestDTO> adoptionRequestDTO);
}
