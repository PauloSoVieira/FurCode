package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.forms.FormFieldCreateDTO1;
import org.mindera.fur.code.dto.forms.FormFieldDTO1;
import org.mindera.fur.code.mapper.adoptionMapper.FormFieldMapper1;
import org.mindera.fur.code.messages.formField.FormFieldMessages;
import org.mindera.fur.code.model.form.FormField1;
import org.mindera.fur.code.repository.AdoptionFormRepository;
import org.mindera.fur.code.repository.FormFieldRepository2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service class for handling operations related to FormField entities.
 */
@Service
public class FormFieldService2 {

    private final Integer MINIMUM_NAME_LENGTH = 2;
    private final Integer MAXIMUM_NAME_LENGTH = 200;
    private final Integer MINIMUM_TYPE_LENGTH = 2;
    private final Integer MAXIMUM_TYPE_LENGTH = 100;

    @Autowired
    private FormFieldRepository2 formFieldRepository2;

    @Autowired
    private AdoptionFormRepository adoptionRepository;

    /**
     * Retrieves all FormFields.
     *
     * @return a list of FormFieldDTO objects
     */
    public List<FormFieldDTO1> getAll() {
        List<FormField1> formField1s = formFieldRepository2.findAll();
        return FormFieldMapper1.INSTANCE.toDTOList(formField1s);
    }

    /**
     * Retrieves a FormField by its ID.
     *
     * @param id the ID of the FormField
     * @return the FormFieldDTO object
     * @throws ResponseStatusException if the FormField is not found
     */
    public FormFieldDTO1 getById(Long id) {
        FormField1 formField1 = formFieldRepository2.findById(id).orElseThrow(() -> new IllegalArgumentException(
                FormFieldMessages.FIELD_NOT_FOUND
        ));
        return FormFieldMapper1.INSTANCE.toDTO(formField1);
    }

    /**
     * Creates a new FormField.
     *
     * @param formFieldCreateDTO1 the FormFieldCreateDTO object containing the details of the FormField to create
     * @return the created FormFieldDTO object
     * @throws ResponseStatusException if the input data is invalid
     */
    public FormFieldDTO1 createField(FormFieldCreateDTO1 formFieldCreateDTO1) {
        validateToCreateField(formFieldCreateDTO1);
        FormField1 formField1 = FormFieldMapper1.INSTANCE.toModel(formFieldCreateDTO1);
        formField1.setAdoptionForm(adoptionRepository.findById(formFieldCreateDTO1.getAdoptionFormId()).orElseThrow(() -> new IllegalArgumentException()));
        formFieldRepository2.save(formField1);
        return FormFieldMapper1.INSTANCE.toDTO(formField1);
    }


    /**
     * Updates an existing FormField.
     *
     * @param id           the ID of the FormField to update
     * @param formFieldDTO1 the FormFieldDTO object containing updated details
     * @return the updated FormFieldDTO object
     * @throws ResponseStatusException if the FormField is not found or input data is invalid
     */
    public FormFieldDTO1 updateField(Long id, FormFieldDTO1 formFieldDTO1) {
        validateField(formFieldDTO1);
        FormField1 formField1 = formFieldRepository2.findById(id).orElseThrow(() -> new IllegalArgumentException(
                FormFieldMessages.FIELD_NOT_FOUND
        ));
        formField1.setId(formFieldDTO1.getId());
        formField1.setName(formFieldDTO1.getName());
        formField1.setType(formFieldDTO1.getType());
        formFieldRepository2.save(formField1);
        return FormFieldMapper1.INSTANCE.toDTO(formField1);
    }


    /**
     * Deletes a FormField by its ID.
     *
     * @param id the ID of the FormField to delete
     * @return the deleted FormFieldDTO object
     * @throws ResponseStatusException if the FormField is not found
     */
    public FormFieldDTO1 deleteField(Long id) {
        FormField1 formField1 = formFieldRepository2.findById(id).orElseThrow(() -> new IllegalArgumentException(
                FormFieldMessages.FIELD_NOT_FOUND
        ));
        formFieldRepository2.delete(formField1);
        return FormFieldMapper1.INSTANCE.toDTO(formField1);
    }

    /**
     * Deletes all FormFields.
     */
    public void deleteAll() {
        formFieldRepository2.deleteAll();
    }


    /**
     * Validates the given FormFieldDTO object.
     *
     * @param formFieldDTO1 the FormFieldDTO object to validate
     * @throws ResponseStatusException if the input data is invalid
     */
    private void validateField(FormFieldDTO1 formFieldDTO1) {
        if (formFieldDTO1.getName() == null || formFieldDTO1.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormFieldMessages.NAME_CANT_BE_EMPTY);
        } else if (formFieldDTO1.getName().length() < MINIMUM_NAME_LENGTH || formFieldDTO1.getName().length() > MAXIMUM_NAME_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormFieldMessages.NAME_MUST_BE_BETWEEN_TWO_AND_TWO_HUNDRED_CHARACTERS);
        }

        if (formFieldDTO1.getType() == null || formFieldDTO1.getType().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormFieldMessages.TYPE_CANT_BE_EMPTY);
        } else if (formFieldDTO1.getType().length() < MINIMUM_TYPE_LENGTH || formFieldDTO1.getType().length() > MAXIMUM_TYPE_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormFieldMessages.NAME_MUST_BE_BETWEEN_TWO_AND_HUNDRED_CHARACTERS);
        }
    }

    private void validateToCreateField(FormFieldCreateDTO1 formFieldDTO) {
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
