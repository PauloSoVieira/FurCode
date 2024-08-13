package org.mindera.fur.code.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.model.AdoptionRequest;

import java.util.List;

/**
 * Mapper class for the AdoptionRequest entity.
 */
@Mapper
public interface AdoptionRequestMapper {

    /**
     * Static instance of the mapper.
     */
    AdoptionRequestMapper INSTANCE = Mappers.getMapper(AdoptionRequestMapper.class);

    /**
     * Converts an AdoptionRequest object to an AdoptionRequestDTO object.
     *
     * @param adoptionRequest The AdoptionRequest object to convert.
     * @return The converted AdoptionRequestDTO object.
     */
    AdoptionRequestDTO toDTO(AdoptionRequest adoptionRequest);

    /**
     * Converts an AdoptionRequestDTO object to an AdoptionRequest object.
     *
     * @param adoptionRequestDTO
     * @return
     */
    AdoptionRequest toModel(AdoptionRequestDTO adoptionRequestDTO);

    /**
     * Converts an AdoptionRequestCreationDTO object to an AdoptionRequest object.
     *
     * @param adoptionRequestCreationDTO
     * @return
     */
    AdoptionRequest toModel(AdoptionRequestCreationDTO adoptionRequestCreationDTO);

    /**
     * Converts a list of AdoptionRequestDTO objects to a list of AdoptionRequest objects.
     *
     * @param adoptionRequest
     * @return
     */
    List<AdoptionRequestDTO> toDTO(List<AdoptionRequest> adoptionRequest);

    /**
     * Converts a list of AdoptionRequest objects to a list of AdoptionRequestDTO objects.
     *
     * @param adoptionRequestDTO
     * @return
     */
    List<AdoptionRequest> toModel(List<AdoptionRequestDTO> adoptionRequestDTO);
}
