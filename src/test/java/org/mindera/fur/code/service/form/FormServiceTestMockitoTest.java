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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
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

    @Spy
    @InjectMocks
    private FormService formService;

    @Nested
    class Validation {

        @Test
        void getForm_withNonExistentId_shouldThrowException() {
            Long nonExistentId = 999L;
            when(formRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            assertThrows(ResponseStatusException.class,
                    () -> formService.getForm(nonExistentId));
        }
        

        @Test
        void submitFormAnswers_withInvalidFieldId_shouldThrowException() {
            Long formId = 1L;
            Form form = new Form();
            form.setId(formId);
            form.setFormFieldAnswers(new ArrayList<>());
            when(formRepository.findById(formId)).thenReturn(Optional.of(form));

            FormAnswerDTO formAnswerDTO = new FormAnswerDTO();
            formAnswerDTO.setFormId(formId);
            FieldAnswerDTO invalidAnswer = new FieldAnswerDTO();
            invalidAnswer.setFieldId(999L); // ID que não existe
            invalidAnswer.setAnswer("Answer");
            formAnswerDTO.setAnswers(List.of(invalidAnswer));

            assertThrows(ResponseStatusException.class,
                    () -> formService.submitFormAnswers(formAnswerDTO));
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
        void addFieldToForm_shouldReturnUpdatedFormDTO() {
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
        void createForm_withNullName_shouldThrowException() {
            FormCreateDTO formCreateDTO = new FormCreateDTO();
            formCreateDTO.setName(null);
            formCreateDTO.setType("TEST");

            assertThrows(ResponseStatusException.class, () -> formService.createForm(formCreateDTO));
        }

        @Test
        void createForm_withEmptyName_shouldThrowException() {
            FormCreateDTO formCreateDTO = new FormCreateDTO();
            formCreateDTO.setName("");
            formCreateDTO.setType("TEST");

            assertThrows(ResponseStatusException.class, () -> formService.createForm(formCreateDTO));
        }

        @Test
        void createForm_withNullType_shouldThrowException() {
            FormCreateDTO formCreateDTO = new FormCreateDTO();
            formCreateDTO.setName("Test Form");
            formCreateDTO.setType(null);

            assertThrows(ResponseStatusException.class, () -> formService.createForm(formCreateDTO));
        }
    }

    @Nested
    class TemplateOperations {
        @Test
        void createFormFromTemplate_withInvalidTemplateName_shouldThrowException() {
            String invalidTemplateName = "invalid-template";

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> formService.createFormFromTemplate(invalidTemplateName));

            try {
                verify(templateLoader, never()).loadTemplate(anyString());
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error loading template: " + invalidTemplateName);
            }
            assert (exception.getReason().contains("Teste: " + invalidTemplateName));
        }

        @Test
        void createFormFromTemplate_withValidTemplate_shouldCreateForm() {

            String validTemplateName = "adoption-template";
            FormTemplateDTO templateDTO = new FormTemplateDTO();
            templateDTO.setName("Adoption Form");
            templateDTO.setType("ADOPTION");
            templateDTO.setFields(new ArrayList<>());

            try {
                when(templateLoader.loadTemplate(validTemplateName)).thenReturn(templateDTO);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error loading template: " + validTemplateName);
            }
            when(formRepository.save(any(Form.class))).thenAnswer(i -> i.getArguments()[0]);

            FormDTO result = formService.createFormFromTemplate(validTemplateName);

            assertNotNull(result);
            assertEquals("Adoption Form", result.getName());
            assertEquals("ADOPTION", result.getType());
            try {
                verify(templateLoader).loadTemplate(validTemplateName);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error loading template: " + validTemplateName);
            }
            verify(formRepository).save(any(Form.class));
        }


        @Test
        void createFormFromTemplate_withEmptyTemplate_shouldCreateEmptyForm() {
            String validTemplateName = "empty-template";
            FormTemplateDTO templateDTO = new FormTemplateDTO();
            templateDTO.setName("Empty Form");
            templateDTO.setType("EMPTY");
            templateDTO.setFields(new ArrayList<>());

            doReturn(true).when(formService).isValidTemplateName(validTemplateName);
            try {
                when(templateLoader.loadTemplate(validTemplateName)).thenReturn(templateDTO);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error loading template: " + validTemplateName);
            }

            when(formRepository.save(any(Form.class))).thenAnswer(invocation -> {
                Form savedForm = invocation.getArgument(0);
                savedForm.setId(1L);
                return savedForm;
            });

            FormDTO result = formService.createFormFromTemplate(validTemplateName);

            assertNotNull(result);
            assertEquals("Empty Form", result.getName());
            assertEquals("EMPTY", result.getType());
            assertEquals(1L, result.getId());
            assertTrue(result.getFormFieldAnswers().isEmpty());
            assertNotNull(result.getCreatedAt());
            assertTrue(result.getCreatedAt().isBefore(LocalDateTime.now()) || result.getCreatedAt().isEqual(LocalDateTime.now()));

            try {
                verify(templateLoader).loadTemplate(validTemplateName);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error loading template: " + validTemplateName);
            }
            verify(formRepository).save(any(Form.class));
        }

        @Test
        void createFormFromTemplate_withNullTemplate_shouldThrowException() {
            String validTemplateName = "null-template";

            doReturn(true).when(formService).isValidTemplateName(validTemplateName);

            try {
                when(templateLoader.loadTemplate(validTemplateName)).thenReturn(null);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error loading template: " + validTemplateName);
            }

            assertThrows(ResponseStatusException.class,
                    () -> formService.createFormFromTemplate(validTemplateName));

            try {
                verify(templateLoader).loadTemplate(validTemplateName);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error loading template: " + validTemplateName);
            }
            verify(formRepository, never()).save(any());
        }

        @Test
        void addFieldToTemplate_withDuplicateQuestion_shouldThrowException() {

            String templateName = "test-template";
            FormTemplateDTO template = new FormTemplateDTO();
            template.setFields(new ArrayList<>());
            template.getFields().add(new FormFieldCreateDTO("TEXT", "Existing Question"));

            doReturn(true).when(formService).isValidTemplateName(templateName);
            try {
                when(templateLoader.loadTemplate(templateName)).thenReturn(template);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error loading template: " + templateName);
            }

            FormFieldCreateDTO newField = new FormFieldCreateDTO("TEXT", "Existing Question");

            assertThrows(ResponseStatusException.class,
                    () -> formService.addFieldToTemplate(templateName, newField));

            try {
                verify(templateLoader).loadTemplate(templateName);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error loading template: " + templateName);
            }
        }
    }

    @Nested
    class CrudForms {
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
        void deleteForm() {
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
        void getFormById_shouldReturnFormDTO() {
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
        void createFormFromTemplate_shouldReturnFormDTO() {
            String templateName = "adoption-template";
            FormTemplateDTO template = new FormTemplateDTO();
            template.setName("Test Template");
            template.setType("TEST");
            template.setFields(new ArrayList<>());

            try {
                when(templateLoader.loadTemplate(templateName)).thenReturn(template);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error loading template");
            }
            when(formRepository.save(any(Form.class))).thenAnswer(invocation -> {
                Form savedForm = invocation.getArgument(0);
                savedForm.setId(1L);
                return savedForm;
            });

            FormDTO result = formService.createFormFromTemplate(templateName);

            assertNotNull(result);
            assertEquals(template.getName(), result.getName());
            assertEquals(template.getType(), result.getType());
            try {
                verify(templateLoader, times(1)).loadTemplate(templateName);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error loading template");
            }
            verify(formRepository, times(1)).save(any(Form.class));
        }

        @Test
        void deleteAllForms() {
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
                formCreateDTO.setFormFieldAnswers(new ArrayList<>());

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
}
