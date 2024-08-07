package org.mindera.fur.code.mapper.adoptionMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.forms.FormFieldCreateDTO1;
import org.mindera.fur.code.dto.forms.FormFieldDTO1;
import org.mindera.fur.code.model.form.FormField1;

import java.util.List;

@Mapper
public interface FormFieldMapper1 {


    FormFieldMapper1 INSTANCE = Mappers.getMapper(FormFieldMapper1.class);

    FormFieldDTO1 toDTO(FormField1 formField1);

    // AdoptionFormCreateDTO DtoToAdoptionCreation(AdoptionForm adoptionForm);

    FormField1 toModel(FormFieldDTO1 formFieldDTO1);

    FormField1 toModel(FormFieldCreateDTO1 formFieldCreateDTO1);


    List<FormFieldDTO1> toDTOList(List<FormField1> formField1s);

}
