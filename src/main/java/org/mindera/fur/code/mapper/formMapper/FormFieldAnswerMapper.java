package org.mindera.fur.code.mapper.formMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.form.FormFieldAnswerDTO;
import org.mindera.fur.code.model.form.FormFieldAnswer;

@Mapper
public interface FormFieldAnswerMapper {
    FormFieldAnswerMapper INSTANCE = Mappers.getMapper(FormFieldAnswerMapper.class);

    @Mapping(source = "form.id", target = "formId")
    @Mapping(source = "formField.id", target = "formFieldId")
    @Mapping(source = "formField.question", target = "question")
    FormFieldAnswerDTO toDTO(FormFieldAnswer formFieldAnswer);

    @Mapping(target = "form", ignore = true)
    @Mapping(target = "formField", ignore = true)
    FormFieldAnswer toModel(FormFieldAnswerDTO formFieldAnswerDTO);
}