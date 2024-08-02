package org.mindera.fur.code.mapper.adoptionMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.formsDTO.AdoptionFormCreateDTO;
import org.mindera.fur.code.dto.formsDTO.AdoptionFormDTO;
import org.mindera.fur.code.model.form.AdoptionForm;

import java.util.List;

@Mapper
public interface AdoptionFormMapper {


    AdoptionFormMapper INSTANCE = Mappers.getMapper(AdoptionFormMapper.class);

    AdoptionFormDTO toDTO(AdoptionForm adoptionForm);

    // AdoptionFormCreateDTO DtoToAdoptionCreation(AdoptionForm adoptionForm);

    AdoptionForm toModel(AdoptionFormDTO adoptionFormDto);

    AdoptionForm toModel(AdoptionFormCreateDTO adoptionFormCreateDto);


    List<AdoptionFormDTO> toDTOList(List<AdoptionForm> adoptionForms);

    List<AdoptionForm> toModelList(List<AdoptionFormDTO> adoptionFormDTOS);
}
