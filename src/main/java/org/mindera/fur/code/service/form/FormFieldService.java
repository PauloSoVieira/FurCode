package org.mindera.fur.code.service.form;

import org.mindera.fur.code.dto.form.FormFieldCreateDTO;
import org.mindera.fur.code.dto.form.FormFieldDTO;
import org.mindera.fur.code.mapper.formMapper.FormFieldMapper;
import org.mindera.fur.code.model.form.FormField;
import org.mindera.fur.code.repository.form.FormFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormFieldService {
    private final FormFieldRepository formFieldRepository;
    private final FormFieldMapper formFieldMapper;

    @Autowired
    public FormFieldService(FormFieldRepository formFieldRepository, FormFieldMapper formFieldMapper) {
        this.formFieldRepository = formFieldRepository;
        this.formFieldMapper = formFieldMapper;
    }

    public FormFieldDTO createFormField(FormFieldCreateDTO createDTO) {
        FormField formField = formFieldMapper.toModel(createDTO);
        FormField savedFormField = formFieldRepository.save(formField);
        return formFieldMapper.toDTO(savedFormField);
    }
}