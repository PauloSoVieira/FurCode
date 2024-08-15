package org.mindera.fur.code.service.form;

import org.mindera.fur.code.dto.form.FormFieldCreateDTO;
import org.mindera.fur.code.dto.form.FormFieldDTO;
import org.mindera.fur.code.mapper.formMapper.FormFieldMapper;
import org.mindera.fur.code.model.form.FormField;
import org.mindera.fur.code.repository.form.FormFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing form fields.
 * This class handles the business logic for creating and managing form fields.
 */
@Service
public class FormFieldService {
    private final FormFieldRepository formFieldRepository;
    private final FormFieldMapper formFieldMapper;

    /**
     * Constructs a new FormFieldService with the necessary dependencies.
     *
     * @param formFieldRepository Repository for FormField entities
     * @param formFieldMapper     Mapper for converting between FormField entities and DTOs
     */
    @Autowired
    public FormFieldService(FormFieldRepository formFieldRepository, FormFieldMapper formFieldMapper) {
        this.formFieldRepository = formFieldRepository;
        this.formFieldMapper = formFieldMapper;
    }

    /**
     * Creates a new form field based on the provided data.
     *
     * @param createDTO DTO containing the data for creating a new form field
     * @return DTO representing the created form field
     */
    public FormFieldDTO createFormField(FormFieldCreateDTO createDTO) {
        FormField formField = formFieldMapper.toModel(createDTO);
        FormField savedFormField = formFieldRepository.save(formField);
        return formFieldMapper.toDTO(savedFormField);
    }
}