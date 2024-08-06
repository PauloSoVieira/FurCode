package org.mindera.fur.code.mockitoTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindera.fur.code.dto.forms.FormFieldCreateDTO;
import org.mindera.fur.code.dto.forms.FormFieldDTO;
import org.mindera.fur.code.mapper.adoptionMapper.FormFieldMapper;
import org.mindera.fur.code.model.form.FormField;
import org.mindera.fur.code.repository.FormFieldRepository;
import org.mindera.fur.code.service.FormFieldService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormFieldServiceTest {


    @Mock
    private FormFieldRepository formFieldRepository;

    @InjectMocks
    private FormFieldService formFieldService;


    /**
     * Tests the creation of a FormField.
     * Verifies that the formFieldRepository's save method is called with the correct parameters.
     */
    @Test
    void createFormField() {

        FormFieldCreateDTO formFieldCreateDTO = new FormFieldCreateDTO("name", "type");


        formFieldService.createField(formFieldCreateDTO);

        FormField formField = FormFieldMapper.INSTANCE.toModel(formFieldCreateDTO);

        verify(formFieldRepository, times(1)).save(formField);
    }

    /**
     * Tests the retrieval of all FormFields.
     * Verifies that the formFieldRepository's findAll method is called with the correct parameters.
     */

    @Test
    void getAllFormFields() {
        List<FormField> formFields = new ArrayList<>();
        formFields.add(new FormField());

        when(formFieldRepository.findAll()).thenReturn(formFields);

        List<FormFieldDTO> formFieldDTOs = formFieldService.getAll();

        assertEquals(formFields.size(), formFieldDTOs.size());
    }

    /**
     * Tests the retrieval of a FormField by its ID.
     * Verifies that the formFieldRepository's findById method is called with the correct parameters.
     */

    @Test
    void getFormFieldById() {

        FormField formField = new FormField();
        formField.setId(1L);
        when(formFieldRepository.findById(1L)).thenReturn(Optional.of(formField));

        FormFieldMapper.INSTANCE.toDTO(formField);

        formFieldService.getById(formField.getId());

        verify(formFieldRepository, times(1)).findById(1L);
        assertEquals(formField.getId(), 1L);


    }

    /**
     * Tests the update of a FormField.
     * Verifies that the formFieldRepository's save method is called with the correct parameters.
     */

    @Test
    void updateFormField() {
        FormField formField = new FormField();
        formField.setId(1L);
        formField.setName("Teste");
        formField.setType("type");
        when(formFieldRepository.findById(1L)).thenReturn(Optional.of(formField));

        FormFieldDTO formFieldUpdateDTO = FormFieldMapper.INSTANCE.toDTO(formField);
        formFieldUpdateDTO.setName("name");

        formFieldService.updateField(1L, formFieldUpdateDTO);

        verify(formFieldRepository, times(1)).save(formField);
    }

    /**
     * Tests the deletion of a FormField.
     * Verifies that the formFieldRepository's delete method is called with the correct parameters.
     */

    @Test
    void deleteFormField() {
        FormField formField = new FormField();
        formField.setId(1L);
        when(formFieldRepository.findById(1L)).thenReturn(Optional.of(formField));

        formFieldService.deleteField(1L);

        verify(formFieldRepository, times(1)).delete(formField);
    }
}