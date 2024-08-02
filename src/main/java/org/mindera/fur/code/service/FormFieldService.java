package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.forms.FormFieldDTO;
import org.mindera.fur.code.mapper.adoptionMapper.FormFieldMapper;
import org.mindera.fur.code.model.form.FormField;
import org.mindera.fur.code.repository.FormFieldRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FormFieldService {


    private FormFieldRepository formFieldRepository;


    public List<FormFieldDTO> getAll() {
        List<FormField> formFields = formFieldRepository.findAll();
        return FormFieldMapper.INSTANCE.toDTOList(formFields);
    }

    public FormFieldDTO getById(Long id) {
        FormField formField = formFieldRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Field not found"));
        return FormFieldMapper.INSTANCE.toDTO(formField);
    }

    public FormFieldDTO createField(FormFieldDTO formFieldDTO) {
        FormField formField = FormFieldMapper.INSTANCE.toModel(formFieldDTO);
        formFieldRepository.save(formField);
        return FormFieldMapper.INSTANCE.toDTO(formField);
    }

    public FormFieldDTO updateField(Long id, FormFieldDTO formFieldDTO) {
        FormField formField = formFieldRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Field not found"));
        formField.setId(formFieldDTO.getId());
        formField.setName(formFieldDTO.getName());
        formField.setType(formFieldDTO.getType());
        formFieldRepository.save(formField);
        return FormFieldMapper.INSTANCE.toDTO(formField);
    }

    public FormFieldDTO deleteField(Long id) {
        FormField formField = formFieldRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Field not found"));
        formFieldRepository.delete(formField);
        return FormFieldMapper.INSTANCE.toDTO(formField);
    }
}
