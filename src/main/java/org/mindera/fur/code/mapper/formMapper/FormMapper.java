package org.mindera.fur.code.mapper.formMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.formTESTEDTO.FormDTO;
import org.mindera.fur.code.dto.formTESTEDTO.FormFieldAnswerDTO;
import org.mindera.fur.code.dto.formTESTEDTO.FormFieldDTO;
import org.mindera.fur.code.model.formTest.Form;
import org.mindera.fur.code.model.formTest.FormField;
import org.mindera.fur.code.model.formTest.FormFieldAnswer;

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