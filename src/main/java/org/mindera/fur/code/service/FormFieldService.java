package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.forms.FormFieldCreateDTO;
import org.mindera.fur.code.dto.forms.FormFieldDTO;
import org.mindera.fur.code.mapper.adoptionMapper.FormFieldMapper;
import org.mindera.fur.code.messages.formField.FormFieldMessages;
import org.mindera.fur.code.model.form.FormField;
import org.mindera.fur.code.repository.AdoptionFormRepository;
import org.mindera.fur.code.repository.FormFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service class for handling operations related to FormField entities.
 */
@Service
public class FormFieldService {

    private final Integer MINIMUM_NAME_LENGTH = 2;
    private final Integer MAXIMUM_NAME_LENGTH = 200;
    private final Integer MINIMUM_TYPE_LENGTH = 2;
    private final Integer MAXIMUM_TYPE_LENGTH = 100;

    @Autowired
    private FormFieldRepository formFieldRepository;

    @Autowired
    private AdoptionFormRepository adoptionRepository;

    /**
     * Retrieves all FormFields.
     *
     * @return a list of FormFieldDTO objects
     */
    public List<FormFieldDTO> getAll() {
        List<FormField> formFields = formFieldRepository.findAll();
        return FormFieldMapper.INSTANCE.toDTOList(formFields);
    }

    /**
     * Retrieves a FormField by its ID.
     *
     * @param id the ID of the FormField
     * @return the FormFieldDTO object
     * @throws ResponseStatusException if the FormField is not found
     */
    public FormFieldDTO getById(Long id) {
        FormField formField = formFieldRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                FormFieldMessages.FIELD_NOT_FOUND
        ));
        return FormFieldMapper.INSTANCE.toDTO(formField);
    }

    /**
     * Creates a new FormField.
     *
     * @param formFieldCreateDTO the FormFieldCreateDTO object containing the details of the FormField to create
     * @return the created FormFieldDTO object
     * @throws ResponseStatusException if the input data is invalid
     */
    public FormFieldDTO createField(FormFieldCreateDTO formFieldCreateDTO) {
        validateToCreateField(formFieldCreateDTO);
        FormField formField = FormFieldMapper.INSTANCE.toModel(formFieldCreateDTO);
        formField.setAdoptionForm(adoptionRepository.findById(formFieldCreateDTO.getAdoptionFormId()).orElseThrow(() -> new IllegalArgumentException()));
        formFieldRepository.save(formField);
        return FormFieldMapper.INSTANCE.toDTO(formField);
    }


    /**
     * Updates an existing FormField.
     *
     * @param id           the ID of the FormField to update
     * @param formFieldDTO the FormFieldDTO object containing updated details
     * @return the updated FormFieldDTO object
     * @throws ResponseStatusException if the FormField is not found or input data is invalid
     */
    public FormFieldDTO updateField(Long id, FormFieldDTO formFieldDTO) {
        validateField(formFieldDTO);
        FormField formField = formFieldRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                FormFieldMessages.FIELD_NOT_FOUND
        ));
        formField.setId(formFieldDTO.getId());
        formField.setName(formFieldDTO.getName());
        formField.setType(formFieldDTO.getType());
        formFieldRepository.save(formField);
        return FormFieldMapper.INSTANCE.toDTO(formField);
    }


    /**
     * Deletes a FormField by its ID.
     *
     * @param id the ID of the FormField to delete
     * @return the deleted FormFieldDTO object
     * @throws ResponseStatusException if the FormField is not found
     */
    public FormFieldDTO deleteField(Long id) {
        FormField formField = formFieldRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                FormFieldMessages.FIELD_NOT_FOUND
        ));
        formFieldRepository.delete(formField);
        return FormFieldMapper.INSTANCE.toDTO(formField);
    }

    /**
     * Deletes all FormFields.
     */
    public void deleteAll() {
        formFieldRepository.deleteAll();
    }


    /**
     * Validates the given FormFieldDTO object.
     *
     * @param formFieldDTO the FormFieldDTO object to validate
     * @throws ResponseStatusException if the input data is invalid
     */
    private void validateField(FormFieldDTO formFieldDTO) {
        if (formFieldDTO.getName() == null || formFieldDTO.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormFieldMessages.NAME_CANT_BE_EMPTY);
        } else if (formFieldDTO.getName().length() < MINIMUM_NAME_LENGTH || formFieldDTO.getName().length() > MAXIMUM_NAME_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormFieldMessages.NAME_MUST_BE_BETWEEN_TWO_AND_TWO_HUNDRED_CHARACTERS);
        }

        if (formFieldDTO.getType() == null || formFieldDTO.getType().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormFieldMessages.TYPE_CANT_BE_EMPTY);
        } else if (formFieldDTO.getType().length() < MINIMUM_TYPE_LENGTH || formFieldDTO.getType().length() > MAXIMUM_TYPE_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormFieldMessages.NAME_MUST_BE_BETWEEN_TWO_AND_HUNDRED_CHARACTERS);
        }
    }

    private void validateToCreateField(FormFieldCreateDTO formFieldDTO) {
        if (formFieldDTO.getName() == null || formFieldDTO.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormFieldMessages.NAME_CANT_BE_EMPTY);
        } else if (formFieldDTO.getName().length() < MINIMUM_NAME_LENGTH || formFieldDTO.getName().length() > MAXIMUM_NAME_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormFieldMessages.NAME_MUST_BE_BETWEEN_TWO_AND_TWO_HUNDRED_CHARACTERS);
        }

        if (formFieldDTO.getType() == null || formFieldDTO.getType().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormFieldMessages.TYPE_CANT_BE_EMPTY);
        } else if (formFieldDTO.getType().length() < MINIMUM_TYPE_LENGTH || formFieldDTO.getType().length() > MAXIMUM_TYPE_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormFieldMessages.NAME_MUST_BE_BETWEEN_TWO_AND_HUNDRED_CHARACTERS);
        }
    }

}
