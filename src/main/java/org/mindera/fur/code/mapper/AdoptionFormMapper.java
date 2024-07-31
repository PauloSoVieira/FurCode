package org.mindera.fur.code.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.AdoptionFormCreateDTO;
import org.mindera.fur.code.dto.AdoptionFormDTO;
import org.mindera.fur.code.model.form.AdoptionForm;

import java.util.List;

@Mapper
public interface AdoptionFormMapper {


    AdoptionFormMapper INSTANCE = Mappers.getMapper(AdoptionFormMapper.class);

    AdoptionFormDTO adoptionToDto(AdoptionForm adoptionForm);

    AdoptionFormCreateDTO DtoToAdoptionCreation(AdoptionForm adoptionForm);

    AdoptionForm toModel(AdoptionFormDTO adoptionFormDto);

    AdoptionForm adoptionToCreate(AdoptionFormCreateDTO adoptionFormCreateDto);

    List<AdoptionFormDTO> adoptionToAdoptionDto(List<AdoptionForm> adoptionForms);

    List<AdoptionForm> adoptionDtoToAdoption(List<AdoptionFormDTO> adoptionFormDTOS);
}
