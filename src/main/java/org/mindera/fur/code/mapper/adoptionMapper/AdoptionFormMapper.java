package org.mindera.fur.code.mapper.adoptionMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.forms.AdoptionFormCreateDTO;
import org.mindera.fur.code.dto.forms.AdoptionFormDTO;
import org.mindera.fur.code.model.form.AdoptionForm;

import java.util.List;

/**
 * Mapper interface for converting between AdoptionForm entity and DTO objects.
 */
@Mapper
public interface AdoptionFormMapper {

    /**
     * Instance of the mapper to be used for dependency injection.
     */
    AdoptionFormMapper INSTANCE = Mappers.getMapper(AdoptionFormMapper.class);

    /**
     * Converts an AdoptionForm entity to an AdoptionFormDTO.
     *
     * @param adoptionForm the AdoptionForm entity.
     * @return the converted AdoptionFormDTO.
     */
    AdoptionFormDTO toDTO(AdoptionForm adoptionForm);

    /**
     * Converts an AdoptionFormDTO to an AdoptionForm entity.
     *
     * @param adoptionFormDto the AdoptionFormDTO.
     * @return the converted AdoptionForm entity.
     */
    AdoptionForm toModel(AdoptionFormDTO adoptionFormDto);

    /**
     * Converts an AdoptionFormCreateDTO to an AdoptionForm entity.
     *
     * @param adoptionFormCreateDto the AdoptionFormCreateDTO.
     * @return the converted AdoptionForm entity.
     */
    AdoptionForm toModel(AdoptionFormCreateDTO adoptionFormCreateDto);

    /**
     * Converts a list of AdoptionForm entities to a list of AdoptionFormDTOs.
     *
     * @param adoptionForms the list of AdoptionForm entities.
     * @return the list of converted AdoptionFormDTOs.
     */
    List<AdoptionFormDTO> toDTOList(List<AdoptionForm> adoptionForms);

    /**
     * Converts a list of AdoptionFormDTOs to a list of AdoptionForm entities.
     *
     * @param adoptionFormDTOS the list of AdoptionFormDTOs.
     * @return the list of converted AdoptionForm entities.
     */
    List<AdoptionForm> toModelList(List<AdoptionFormDTO> adoptionFormDTOS);
}
