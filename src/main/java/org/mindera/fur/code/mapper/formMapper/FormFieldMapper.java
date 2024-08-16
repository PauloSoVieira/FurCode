package org.mindera.fur.code.mapper.formMapper;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.form.FormFieldCreateDTO;
import org.mindera.fur.code.dto.form.FormFieldDTO;
import org.mindera.fur.code.model.form.FormField;

import java.util.List;

@Schema(name = "Form Field Mapper", description = "Mapper for converting between FormField and FormFieldDTO")
@Mapper(componentModel = "spring")
public interface FormFieldMapper {

    /**
     * The instance of the FormFieldMapper.
     */
    FormFieldMapper INSTANCE = Mappers.getMapper(FormFieldMapper.class);

    /**
     * Converts a FormField to a FormFieldDTO.
     *
     * @param formField
     * @return
     */
    @Schema(name = "toDTO", description = "Converts a FormField to a FormFieldDTO")
    FormFieldDTO toDTO(FormField formField);

    /**
     * Converts a FormFieldDTO to a FormField.
     *
     * @param formFieldDTO
     * @return
     */
    FormField toModel(FormFieldDTO formFieldDTO);

    /**
     * Converts a list of FormFieldDTO to a list of FormField.
     *
     * @param formFields
     * @return
     */
    List<FormFieldDTO> toDTOList(List<FormField> formFields);

    /**
     * Converts a FormFieldCreateDTO to a FormField.
     *
     * @param createDTO
     * @return
     */
    FormField toModel(FormFieldCreateDTO createDTO);
}