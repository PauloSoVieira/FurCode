package org.mindera.fur.code.mapper.formMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.form.FormFieldCreateDTO;
import org.mindera.fur.code.dto.form.FormFieldDTO;
import org.mindera.fur.code.model.form.FormField;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FormFieldMapper {
    FormFieldMapper INSTANCE = Mappers.getMapper(FormFieldMapper.class);

    FormFieldDTO toDTO(FormField formField);
    FormField toModel(FormFieldDTO formFieldDTO);
    List<FormFieldDTO> toDTOList(List<FormField> formFields);

    FormField toModel(FormFieldCreateDTO createDTO);
}