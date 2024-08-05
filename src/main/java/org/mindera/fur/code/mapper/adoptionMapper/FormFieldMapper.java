package org.mindera.fur.code.mapper.adoptionMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.formsDTO.FormFieldCreateDTO;
import org.mindera.fur.code.dto.formsDTO.FormFieldDTO;
import org.mindera.fur.code.model.form.FormField;

import java.util.List;

@Mapper
public interface FormFieldMapper {


    FormFieldMapper INSTANCE = Mappers.getMapper(FormFieldMapper.class);

    FormFieldDTO toDTO(FormField formField);

    // AdoptionFormCreateDTO DtoToAdoptionCreation(AdoptionForm adoptionForm);

    FormField toModel(FormFieldDTO formFieldDTO);

    FormField toModel(FormFieldCreateDTO formFieldCreateDTO);


    List<FormFieldDTO> toDTOList(List<FormField> formFields);

}
