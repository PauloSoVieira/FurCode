package org.mindera.fur.code.mapper;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.model.AdoptionRequest;

import java.util.List;

/**
 * Mapper class for the AdoptionRequest entity.
 */
@Mapper
@Schema(description = "The adoption request mapper")
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
    @Mapping(source = "person.id", target = "personId")
    @Mapping(source = "pet.id", target = "petId")
    @Mapping(source = "shelter.id", target = "shelterId")
    AdoptionRequestDTO toDTO(AdoptionRequest adoptionRequest);

    /**
     * Converts an AdoptionRequestDTO object to an AdoptionRequest object.
     *
     * @param adoptionRequestDTO The AdoptionRequestDTO object to convert.
     * @return The converted AdoptionRequest object.
     */
    AdoptionRequest toModel(AdoptionRequestDTO adoptionRequestDTO);

    /**
     * Converts an AdoptionRequestCreationDTO object to an AdoptionRequest object.
     *
     * @param adoptionRequestCreationDTO The AdoptionRequestCreationDTO object to convert.
     * @return The converted AdoptionRequest object.
     */
    @Mapping(source = "personId", target = "person.id")
    @Mapping(source = "petId", target = "pet.id")
    @Mapping(source = "shelterId", target = "shelter.id")
    AdoptionRequest toModel(AdoptionRequestCreationDTO adoptionRequestCreationDTO);

    /**
     * Converts a list of AdoptionRequestDTO objects to a list of AdoptionRequest objects.
     *
     * @param adoptionRequest The list of AdoptionRequestDTO objects to convert.
     * @return The converted list of AdoptionRequest objects.
     */
    List<AdoptionRequestDTO> toDTO(List<AdoptionRequest> adoptionRequest);

    /**
     * Converts a list of AdoptionRequest objects to a list of AdoptionRequestDTO objects.
     *
     * @param adoptionRequestDTO The list of AdoptionRequest objects to convert.
     * @return The converted list of AdoptionRequestDTO objects.
     */
    List<AdoptionRequest> toModel(List<AdoptionRequestDTO> adoptionRequestDTO);
}
