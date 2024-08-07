package org.mindera.fur.code.mapper.formMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.formTESTEDTO.FormDTO;
import org.mindera.fur.code.model.formTest.Form;

import java.util.List;

@Mapper
public interface FormMapper {
    FormMapper INSTANCE = Mappers.getMapper(FormMapper.class);

    FormDTO toDTO(Form form);
    Form toModel(FormDTO formDTO);

    List<FormDTO> toDTOList(List<Form> forms);
}