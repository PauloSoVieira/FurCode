package org.mindera.fur.code.mapper.formMapper;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.form.FormFieldAnswerDTO;
import org.mindera.fur.code.model.form.FormFieldAnswer;

@Schema(name = "Form Field Answer Mapper", description = "Mapper for converting between FormFieldAnswer and FormFieldAnswerDTO")
@Mapper
public interface FormFieldAnswerMapper {
    FormFieldAnswerMapper INSTANCE = Mappers.getMapper(FormFieldAnswerMapper.class);

    /**
     * Converts a FormFieldAnswerDTO to a FormFieldAnswer.
     *
     * @param formFieldAnswer
     * @return
     */

    @Mapping(source = "form.id", target = "formId")
    @Mapping(source = "formField.id", target = "formFieldId")
    @Mapping(source = "formField.question", target = "question")
    FormFieldAnswerDTO toDTO(FormFieldAnswer formFieldAnswer);

    /**
     * Converts a FormFieldAnswer to a FormFieldAnswerDTO.
     *
     * @param formFieldAnswerDTO
     * @return
     */
    @Mapping(target = "form", ignore = true)
    @Mapping(target = "formField", ignore = true)
    FormFieldAnswer toModel(FormFieldAnswerDTO formFieldAnswerDTO);
}