package org.mindera.fur.code.mapper.formMapper;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.form.FormCreateDTO;
import org.mindera.fur.code.dto.form.FormDTO;
import org.mindera.fur.code.dto.form.FormFieldAnswerDTO;
import org.mindera.fur.code.model.form.Form;
import org.mindera.fur.code.model.form.FormFieldAnswer;

import java.util.List;

@Schema(name = "Form Mapper", description = "Mapper for converting between Form and FormDTO")
@Mapper
public interface FormMapper {
    /**
     * The instance of the FormMapper.
     */
    FormMapper INSTANCE = Mappers.getMapper(FormMapper.class);

    /**
     * Converts a Form to a FormDTO.
     *
     * @param form
     * @return
     */
    @Schema(name = "toDTO", description = "Converts a Form to a FormDTO")
    @Mapping(target = "formFieldAnswers", source = "formFieldAnswers")
    FormDTO toDTO(Form form);

    /**
     * Converts a FormFieldAnswer to a FormFieldAnswerDTO.
     *
     * @param formFieldAnswer
     * @return
     */
    @Schema(name = "toFormFieldAnswerDTO", description = "Converts a FormFieldAnswer to a FormFieldAnswerDTO")
    @Mapping(target = "formId", source = "form.id")
    @Mapping(target = "formFieldId", source = "formField.id")
    @Mapping(target = "question", source = "formField.question")
    FormFieldAnswerDTO toFormFieldAnswerDTO(FormFieldAnswer formFieldAnswer);

    /**
     * Converts a list of Form to a list of FormDTO.
     *
     * @param forms
     * @return
     */
    List<FormDTO> toDTOList(List<Form> forms);

    /**
     * Converts a FormCreateDTO to a Form.
     *
     * @param formCreateDTO
     * @return
     */
    Form toModelFromCreateDTO(FormCreateDTO formCreateDTO);
}