package org.mindera.fur.code.mockitoTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindera.fur.code.dto.forms.FormFieldCreateDTO1;
import org.mindera.fur.code.dto.forms.FormFieldDTO1;
import org.mindera.fur.code.mapper.adoptionMapper.FormFieldMapper1;
import org.mindera.fur.code.model.form.FormField1;
import org.mindera.fur.code.repository.FormFieldRepository2;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormField1ServiceTest {


    @Mock
    private FormFieldRepository2 formFieldRepository2;

    @InjectMocks
    private FormFieldService2 formFieldService2;


    /**
     * Tests the creation of a FormField.
     * Verifies that the formFieldRepository's save method is called with the correct parameters.
     */
    @Test
    void createFormField() {

        FormFieldCreateDTO1 formFieldCreateDTO1 = new FormFieldCreateDTO1("name", "type");


        formFieldService2.createField(formFieldCreateDTO1);

        FormField1 formField1 = FormFieldMapper1.INSTANCE.toModel(formFieldCreateDTO1);

        verify(formFieldRepository2, times(1)).save(formField1);
    }

    /**
     * Tests the retrieval of all FormFields.
     * Verifies that the formFieldRepository's findAll method is called with the correct parameters.
     */

    @Test
    void getAllFormFields() {
        List<FormField1> formField1s = new ArrayList<>();
        formField1s.add(new FormField1());

        when(formFieldRepository2.findAll()).thenReturn(formField1s);

        List<FormFieldDTO1> formFieldDTO1s = formFieldService2.getAll();

        assertEquals(formField1s.size(), formFieldDTO1s.size());
    }

    /**
     * Tests the retrieval of a FormField by its ID.
     * Verifies that the formFieldRepository's findById method is called with the correct parameters.
     */

    @Test
    void getFormFieldById() {

        FormField1 formField1 = new FormField1();
        formField1.setId(1L);
        when(formFieldRepository2.findById(1L)).thenReturn(Optional.of(formField1));

        FormFieldMapper1.INSTANCE.toDTO(formField1);

        formFieldService2.getById(formField1.getId());

        verify(formFieldRepository2, times(1)).findById(1L);
        assertEquals(formField1.getId(), 1L);


    }

    /**
     * Tests the update of a FormField.
     * Verifies that the formFieldRepository's save method is called with the correct parameters.
     */

    @Test
    void updateFormField() {
        FormField1 formField1 = new FormField1();
        formField1.setId(1L);
        formField1.setName("Teste");
        formField1.setType("type");
        when(formFieldRepository2.findById(1L)).thenReturn(Optional.of(formField1));

        FormFieldDTO1 formFieldUpdateDTO = FormFieldMapper1.INSTANCE.toDTO(formField1);
        formFieldUpdateDTO.setName("name");

        formFieldService2.updateField(1L, formFieldUpdateDTO);

        verify(formFieldRepository2, times(1)).save(formField1);
    }

    /**
     * Tests the deletion of a FormField.
     * Verifies that the formFieldRepository's delete method is called with the correct parameters.
     */

    @Test
    void deleteFormField() {
        FormField1 formField1 = new FormField1();
        formField1.setId(1L);
        when(formFieldRepository2.findById(1L)).thenReturn(Optional.of(formField1));

        formFieldService2.deleteField(1L);

        verify(formFieldRepository2, times(1)).delete(formField1);
    }
}