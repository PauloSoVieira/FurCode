package org.mindera.fur.code.mockitoTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindera.fur.code.dto.forms.AdoptionFormCreateDTO;
import org.mindera.fur.code.dto.forms.AdoptionFormDTO;
import org.mindera.fur.code.mapper.adoptionMapper.AdoptionFormMapper;
import org.mindera.fur.code.model.form.AdoptionForm;
import org.mindera.fur.code.model.form.FormField1;
import org.mindera.fur.code.repository.AdoptionFormRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdoptionFormServiceTest {

    @Mock
    private AdoptionFormRepository adoptionFormRepository;

    @InjectMocks
    private AdoptionFormService adoptionFormService;

    /**
     * Test case for creating an adoption form.
     */
    @Test
    void createAdoptionForm() {
        // Arrange
        AdoptionFormCreateDTO adoptionFormCreateDTO = new AdoptionFormCreateDTO("jojo", 1L, 1L, 1L);
        AdoptionForm adoptionForm = AdoptionFormMapper.INSTANCE.toModel(adoptionFormCreateDTO);

        // Act
        adoptionFormService.createAdoptionForm(adoptionFormCreateDTO);

        // Assert
        verify(adoptionFormRepository, times(1)).save(adoptionForm);
    }

    /**
     * Test case for retrieving an adoption form by ID.
     */
    @Test
    void getAdoptionFormById() {
        // Arrange
        AdoptionForm adoptionForm = new AdoptionForm();
        adoptionForm.setId(1L);
        adoptionForm.setName("jojo");
        adoptionForm.setShelterId(1L);
        adoptionForm.setFormField1s(Set.of(new FormField1()));

        when(adoptionFormRepository.findById(1L)).thenReturn(Optional.of(adoptionForm));

        // Act
        AdoptionFormDTO result = adoptionFormService.getById(1L);

        // Assert
        verify(adoptionFormRepository, times(1)).findById(1L);
        assertEquals("jojo", result.getName());
    }

    /**
     * Test case for retrieving all adoption forms.
     */
    @Test
    void getAllAdoptionForms() {
        // Arrange
        AdoptionForm adoptionForm = new AdoptionForm();
        adoptionForm.setId(1L);
        adoptionForm.setName("jojo");
        adoptionForm.setShelterId(1L);
        adoptionForm.setFormField1s(Set.of(new FormField1()));

        when(adoptionFormRepository.findAll()).thenReturn(List.of(adoptionForm));

        // Act
        List<AdoptionFormDTO> result = adoptionFormService.getAll();

        // Assert
        verify(adoptionFormRepository, times(1)).findAll();
        assertEquals(1, result.size());
        assertEquals("jojo", result.get(0).getName());
    }

    /**
     * Test case for updating an adoption form.
     */
    @Test
    void updateAdoptionForm() {
        // Arrange
        AdoptionForm adoptionForm = new AdoptionForm();
        adoptionForm.setId(1L);
        adoptionForm.setName("jojo");
        adoptionForm.setShelterId(1L);
        adoptionForm.setFormField1s(Set.of(new FormField1()));

        when(adoptionFormRepository.findById(1L)).thenReturn(Optional.of(adoptionForm));
        AdoptionFormDTO adoptionFormDTO = AdoptionFormMapper.INSTANCE.toDTO(adoptionForm);

        // Act
        adoptionFormService.updateAdoptionForm(adoptionFormDTO, 1L);

        // Assert
        verify(adoptionFormRepository, times(1)).save(adoptionForm);
    }

    /**
     * Test case for deleting an adoption form.
     */
    @Test
    void deleteAdoptionForm() {
        // Arrange
        AdoptionForm adoptionForm = new AdoptionForm();
        adoptionForm.setId(1L);
        adoptionForm.setName("jojo");
        adoptionForm.setShelterId(1L);
        adoptionForm.setFormField1s(Set.of(new FormField1()));

        when(adoptionFormRepository.findById(1L)).thenReturn(Optional.of(adoptionForm));

        // Act
        adoptionFormService.delete(1L);

        // Assert
        verify(adoptionFormRepository, times(1)).delete(adoptionForm);
    }
}
