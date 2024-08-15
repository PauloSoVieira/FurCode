package org.mindera.fur.code.service.form;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindera.fur.code.controller.form.TemplateLoaderUtil;
import org.mindera.fur.code.dto.form.*;
import org.mindera.fur.code.model.form.Form;
import org.mindera.fur.code.model.form.FormField;
import org.mindera.fur.code.model.form.FormFieldAnswer;
import org.mindera.fur.code.repository.form.FormFieldAnswerRepository;
import org.mindera.fur.code.repository.form.FormFieldRepository;
import org.mindera.fur.code.repository.form.FormRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormServiceTestMockitoTest {

    @Mock
    private FormRepository formRepository;

    @Mock
    private FormFieldRepository formFieldRepository;

    @Mock
    private FormFieldAnswerRepository formFieldAnswerRepository;

    @Mock
    private FormFieldService formFieldService;

    @Mock
    private TemplateLoaderUtil templateLoader;

    @InjectMocks
    private FormService formService;

    @Test
    void getAllForms_shouldReturnListOfFormDTOs() {
        List<Form> forms = new ArrayList<>();
        forms.add(new Form());
        forms.add(new Form());

        when(formRepository.findAll()).thenReturn(forms);

        List<FormDTO> result = formService.getAllForms();

        assertNotNull(result);
        assertEquals(forms.size(), result.size());
        verify(formRepository, times(1)).findAll();
    }

    @Test
    void deleteForm_shouldReturnDeletedFormDTO() {
        Long formId = 1L;
        Form form = new Form();
        form.setId(formId);
        form.setName("Test Form");

        when(formRepository.findById(formId)).thenReturn(Optional.of(form));

        FormDTO result = formService.deleteForm(formId);

        assertNotNull(result);
        assertEquals(form.getId(), result.getId());
        assertEquals(form.getName(), result.getName());
        verify(formRepository, times(1)).findById(formId);
        verify(formRepository, times(1)).delete(form);
    }

    @Test
    void deleteForm_shouldThrowException_whenFormNotFound() {
        Long formId = 1L;
        when(formRepository.findById(formId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> formService.deleteForm(formId));
        verify(formRepository, times(1)).findById(formId);
        verify(formRepository, never()).delete(any(Form.class));
    }

    @Test
    void addFieldToForm_shouldReturnUpdatedFormDTO() throws Exception {
        Long formId = 1L;
        Form form = new Form();
        form.setId(formId);
        form.setFormFieldAnswers(new ArrayList<>());

        FormFieldCreateDTO newField = new FormFieldCreateDTO();
        newField.setQuestion("New Question");
        newField.setFieldType("TEXT");

        FormField formField = new FormField();
        formField.setId(1L);
        formField.setQuestion(newField.getQuestion());
        formField.setFieldType(newField.getFieldType());

        when(formRepository.findById(formId)).thenReturn(Optional.of(form));
        when(formFieldRepository.save(any(FormField.class))).thenReturn(formField);
        when(formRepository.save(any(Form.class))).thenReturn(form);

        FormDTO result = formService.addFieldToForm(formId, newField);

        assertNotNull(result);
        assertEquals(form.getId(), result.getId());
        verify(formRepository, times(1)).findById(formId);
        verify(formFieldRepository, times(1)).save(any(FormField.class));
        verify(formRepository, times(1)).save(any(Form.class));
    }

    @Test
    void createFormFromTemplate_shouldReturnFormDTO() throws IOException {
        String templateName = "adoption-template";
        FormTemplateDTO template = new FormTemplateDTO();
        template.setName("Test Template");
        template.setType("TEST");
        template.setFields(new ArrayList<>());

        when(templateLoader.loadTemplate(templateName)).thenReturn(template);
        when(formRepository.save(any(Form.class))).thenAnswer(invocation -> {
            Form savedForm = invocation.getArgument(0);
            savedForm.setId(1L);
            return savedForm;
        });

        FormDTO result = formService.createFormFromTemplate(templateName);

        assertNotNull(result);
        assertEquals(template.getName(), result.getName());
        assertEquals(template.getType(), result.getType());
        verify(templateLoader, times(1)).loadTemplate(templateName);
        verify(formRepository, times(1)).save(any(Form.class));
    }

    @Test
    void submitFormAnswers_shouldReturnUpdatedFormDTO() {
        Long formId = 1L;
        FormAnswerDTO formAnswerDTO = new FormAnswerDTO();
        formAnswerDTO.setFormId(formId);
        List<FieldAnswerDTO> answers = new ArrayList<>();
        FieldAnswerDTO fieldAnswer = new FieldAnswerDTO();
        fieldAnswer.setFieldId(1L);
        fieldAnswer.setAnswer("Test Answer");
        answers.add(fieldAnswer);
        formAnswerDTO.setAnswers(answers);

        Form form = new Form();
        form.setId(formId);
        List<FormFieldAnswer> formFieldAnswers = new ArrayList<>();
        FormFieldAnswer formFieldAnswer = new FormFieldAnswer();
        formFieldAnswer.setFormField(new FormField());
        formFieldAnswer.getFormField().setId(1L);
        formFieldAnswers.add(formFieldAnswer);
        form.setFormFieldAnswers(formFieldAnswers);

        when(formRepository.findById(formId)).thenReturn(Optional.of(form));
        when(formRepository.save(any(Form.class))).thenReturn(form);

        FormDTO result = formService.submitFormAnswers(formAnswerDTO);

        assertNotNull(result);
        assertEquals(form.getId(), result.getId());
        verify(formRepository, times(1)).findById(formId);
        verify(formRepository, times(1)).save(any(Form.class));
    }

    @Test
    void deleteAllForms_shouldCallRepositoryMethod() {
        formService.deleteAllForms();
        verify(formRepository, times(1)).deleteAll();
    }

    @Nested
    class CreateForm {
        @Test
        void createForm_shouldReturnFormDTO() {
            FormCreateDTO formCreateDTO = new FormCreateDTO();
            formCreateDTO.setName("adoption-template");
            formCreateDTO.setCreatedAt(LocalDateTime.now());
            formCreateDTO.setType("TEST");
            formCreateDTO.setFormFieldAnswers(new ArrayList<>()); // Initialize with an empty list

            Form form = new Form();
            form.setId(1L);
            form.setName(formCreateDTO.getName());
            form.setCreatedAt(formCreateDTO.getCreatedAt());
            form.setType(formCreateDTO.getType());
            form.setFormFieldAnswers(new ArrayList<>());

            when(formRepository.save(any(Form.class))).thenReturn(form);

            FormDTO result = formService.createForm(formCreateDTO);

            assertNotNull(result);
            assertEquals(form.getId(), result.getId());
            assertEquals(form.getName(), result.getName());
            assertEquals(form.getType(), result.getType());
            assertEquals(form.getCreatedAt(), result.getCreatedAt());
            assertNotNull(result.getFormFieldAnswers());
            assertTrue(result.getFormFieldAnswers().isEmpty());
            verify(formRepository, times(1)).save(any(Form.class));
        }

        @Test
        void getForm_shouldReturnFormDTO() {
            Long formId = 1L;
            Form form = new Form();
            form.setId(formId);
            form.setName("Test Form");

            when(formRepository.findById(formId)).thenReturn(Optional.of(form));


            FormDTO result = formService.getForm(formId);


            assertNotNull(result);
            assertEquals(form.getId(), result.getId());
            assertEquals(form.getName(), result.getName());
            verify(formRepository, times(1)).findById(formId);
        }

        @Test
        void getForm_shouldThrowException_whenFormNotFound() {
            Long formId = 1L;
            when(formRepository.findById(formId)).thenReturn(Optional.empty());


            assertThrows(RuntimeException.class, () -> formService.getForm(formId));
            verify(formRepository, times(1)).findById(formId);
        }

        @Test
        void createFormFromTemplate_shouldReturnFormDTO() throws IOException {
            String templateName = "adoption-template";
            FormTemplateDTO template = new FormTemplateDTO();
            template.setName("Test Template");
            template.setType("TEST");
            template.setFields(new ArrayList<>());

            when(templateLoader.loadTemplate(templateName)).thenReturn(template);
            when(formRepository.save(any(Form.class))).thenAnswer(invocation -> {
                Form savedForm = invocation.getArgument(0);
                savedForm.setId(1L);
                return savedForm;
            });

            FormDTO result = formService.createFormFromTemplate(templateName);

            assertNotNull(result);
            assertEquals(template.getName(), result.getName());
            assertEquals(template.getType(), result.getType());
            verify(templateLoader, times(1)).loadTemplate(templateName);
            verify(formRepository, times(1)).save(any(Form.class));
        }

        @Test
        void submitFormAnswers_shouldReturnUpdatedFormDTO() {
            Long formId = 1L;
            FormAnswerDTO formAnswerDTO = new FormAnswerDTO();
            formAnswerDTO.setFormId(formId);
            formAnswerDTO.setAnswers(new ArrayList<>());

            Form form = new Form();
            form.setId(formId);
            form.setFormFieldAnswers(new ArrayList<>());

            when(formRepository.findById(formId)).thenReturn(Optional.of(form));
            when(formRepository.save(any(Form.class))).thenReturn(form);

            FormDTO result = formService.submitFormAnswers(formAnswerDTO);


            assertNotNull(result);
            assertEquals(form.getId(), result.getId());
            verify(formRepository, times(1)).findById(formId);
            verify(formRepository, times(1)).save(any(Form.class));
        }
    }

}
