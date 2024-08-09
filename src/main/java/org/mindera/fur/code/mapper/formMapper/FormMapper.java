package org.mindera.fur.code.mapper.formMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.form.FormDTO;
import org.mindera.fur.code.dto.form.FormFieldAnswerDTO;
import org.mindera.fur.code.model.form.Form;
import org.mindera.fur.code.model.form.FormFieldAnswer;

import java.util.List;

@Mapper
public interface FormMapper {
    FormMapper INSTANCE = Mappers.getMapper(FormMapper.class);

    @Mapping(target = "formFieldAnswers", source = "formFieldAnswers")
    FormDTO toDTO(Form form);

    @Mapping(target = "formId", source = "form.id")
    @Mapping(target = "formFieldId", source = "formField.id")
    @Mapping(target = "question", source = "formField.question")
    FormFieldAnswerDTO toFormFieldAnswerDTO(FormFieldAnswer formFieldAnswer);

    List<FormDTO> toDTOList(List<Form> forms);
}