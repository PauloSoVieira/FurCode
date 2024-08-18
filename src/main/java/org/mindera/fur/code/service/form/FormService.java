package org.mindera.fur.code.service.form;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import org.mindera.fur.code.controller.form.TemplateLoaderUtil;
import org.mindera.fur.code.dto.form.*;
import org.mindera.fur.code.mapper.formMapper.FormMapper;
import org.mindera.fur.code.messages.form.FormMessages;
import org.mindera.fur.code.messages.formField.FormFieldMessages;
import org.mindera.fur.code.model.form.Form;
import org.mindera.fur.code.model.form.FormField;
import org.mindera.fur.code.model.form.FormFieldAnswer;
import org.mindera.fur.code.repository.form.FormFieldAnswerRepository;
import org.mindera.fur.code.repository.form.FormFieldRepository;
import org.mindera.fur.code.repository.form.FormRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for managing forms and form templates.
 * This class handles the business logic for creating, updating, and deleting forms and form templates.
 */
@Service
public class FormService {
    private static final Logger logger = LoggerFactory.getLogger(FormService.class);

    private final FormRepository formRepository;
    private final FormFieldRepository formFieldRepository;
    private final FormFieldAnswerRepository formFieldAnswerRepository;
    private final FormFieldService formFieldService;
    private final TemplateLoaderUtil templateLoader;

    /**
     * Constructs a new FormService with the necessary dependencies.
     */
    @Autowired
    public FormService(FormRepository formRepository, FormFieldRepository formFieldRepository, FormFieldAnswerRepository formFieldAnswerRepository, FormFieldService formFieldService, TemplateLoaderUtil templateLoader) {
        this.formRepository = formRepository;
        this.formFieldRepository = formFieldRepository;
        this.formFieldAnswerRepository = formFieldAnswerRepository;
        this.formFieldService = formFieldService;
        this.templateLoader = templateLoader;
    }


    /**
     * Creates a new form based on the provided data.
     *
     * @param formCreateDTO DTO containing the data for creating a new form
     * @return DTO representing the created form
     */
    @Operation(summary = "Create a new form", description = "Creates a new form based on the provided data")
    @Transactional
    public FormDTO createForm(FormCreateDTO formCreateDTO) {
        if (formCreateDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormMessages.FORM_CANT_BE_NULL);
        }
        if (formCreateDTO.getName() == null || formCreateDTO.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormMessages.FORM_NAME_CANT_BE_NULL_OR_EMPTY);
        }
        if (formCreateDTO.getType() == null || formCreateDTO.getType().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormMessages.FORM_TYPE_CANT_BE_NULL_OR_EMPTY);
        }
        if (formCreateDTO.getInitialField() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormMessages.FORM_FIELD_CANT_BE_NULL);
        }

        Form form = new Form();
        form.setName(formCreateDTO.getName());
        form.setType(formCreateDTO.getType());
        form.setCreatedAt(formCreateDTO.getCreatedAt() != null ? formCreateDTO.getCreatedAt() : LocalDateTime.now());

        List<FormFieldAnswer> formFieldAnswers = new ArrayList<>();
        FormField formField = new FormField();
        formField.setFieldType(formCreateDTO.getInitialField().getFieldType());
        formField.setQuestion(formCreateDTO.getInitialField().getQuestion());
        formField.setForm(form);

        FormFieldAnswer answer = new FormFieldAnswer();
        answer.setFormField(formField);
        answer.setAnswer("");
        answer.setForm(form);
        formFieldAnswers.add(answer);

        form.setFormFieldAnswers(formFieldAnswers);
        form.setFields(List.of(formField));

        Form savedForm = formRepository.save(form);
        return FormMapper.INSTANCE.toDTO(savedForm);
    }

    /**
     * Retrieves a form by its ID.
     *
     * @param formId ID of the form to retrieve
     * @return DTO representing the retrieved form
     * @throws ResponseStatusException if the form is not found
     */
    @Operation(summary = "Get a form", description = "Retrieves a form by its ID")
    public FormDTO getForm(Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, FormMessages.FORM_ID_NOT_FOUND));
        return FormMapper.INSTANCE.toDTO(form);
    }

    /**
     * Checks if the provided template name is valid.
     *
     * @param templateName Name of the template to validate
     * @return true if the template name is valid, false otherwise
     */
    @Operation(summary = "Check if a template name is valid", description = "Checks if the provided template name is valid")
    boolean isValidTemplateName(String templateName) {

        List<String> validTemplateNames = Arrays.asList(FormMessages.DONATION_TEMPLATE, FormMessages.ADOPTION_TEMPLATE);
        return validTemplateNames.contains(templateName);
    }

    /**
     * Creates a new form from a template.
     *
     * @param templateName Name of the template to use
     * @return DTO representing the created form
     * @throws ResponseStatusException if the template is not found or there's an error creating the form
     */
    @Operation(summary = "Create a form from a template", description = "Creates a new form from a template")
    @Transactional
    public FormDTO createFormFromTemplate(String templateName) {
        if (!isValidTemplateName(templateName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormMessages.TEMPLATE_NAME_INVALID + ": " + templateName);
        }

        try {
            FormTemplateDTO template = templateLoader.loadTemplate(templateName);

            if (template == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormMessages.TEMPLATE_NOT_FOUND + ": " + templateName);
            }

            List<FormField> existingFields = formFieldRepository.findByQuestionIn(
                    template.getFields().stream().map(FormFieldCreateDTO::getQuestion).collect(Collectors.toList())
            );

            Map<String, FormField> existingFieldMap = existingFields.stream()
                    .collect(Collectors.toMap(FormField::getQuestion, field -> field));

            Form form = new Form();
            form.setName(template.getName());
            form.setCreatedAt(LocalDateTime.now());
            form.setType(template.getType());

            List<FormFieldAnswer> formFieldAnswers = new ArrayList<>();
            for (FormFieldCreateDTO fieldDTO : template.getFields()) {
                FormField formField = existingFieldMap.get(fieldDTO.getQuestion());
                if (formField == null) {
                    formField = new FormField();
                    formField.setFieldType(fieldDTO.getFieldType());
                    formField.setQuestion(fieldDTO.getQuestion());
                    formField = formFieldRepository.save(formField);
                }

                FormFieldAnswer answer = new FormFieldAnswer();
                answer.setFormField(formField);
                answer.setAnswer("");
                answer.setForm(form);
                formFieldAnswers.add(answer);
            }

            form.setFormFieldAnswers(formFieldAnswers);
            Form savedForm = formRepository.save(form);
            return FormMapper.INSTANCE.toDTO(savedForm);
        } catch (Exception e) {
            logger.error(FormMessages.ERROR_IN_CREATING_FORM_FROM_TEMPLATE, e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormMessages.ERROR_IN_CREATING_FORM_FROM_TEMPLATE + ": " + templateName);
        }
    }

    /**
     * Submits answers to a form.
     *
     * @param formAnswerDTO DTO containing the answers to submit
     * @return DTO representing the updated form
     * @throws ResponseStatusException if the form is not found or there's an error updating the form
     */
    @Operation(summary = "Submit answers to a form", description = "Submits answers to a form")
    @Transactional
    public FormDTO submitFormAnswers(FormAnswerDTO formAnswerDTO) {
        Form form = formRepository.findById(formAnswerDTO.getFormId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, FormMessages.FORM_ID_NOT_FOUND));

        Map<Long, FormFieldAnswer> answerMap = form.getFormFieldAnswers().stream()
                .collect(Collectors.toMap(answer -> answer.getFormField().getId(), answer -> answer));

        for (FieldAnswerDTO fieldAnswer : formAnswerDTO.getAnswers()) {
            FormFieldAnswer answer = answerMap.get(fieldAnswer.getFieldId());
            if (answer == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, FormMessages.FORM_ID_NOT_FOUND + ": " + fieldAnswer.getFieldId());
            }
            answer.setAnswer(fieldAnswer.getAnswer());
        }

        Form savedForm = formRepository.save(form);
        return FormMapper.INSTANCE.toDTO(savedForm);
    }

    /**
     * Adds a field to a form template.
     *
     * @param templateName Name of the template to add the field to
     * @param newField     DTO containing the data for creating the new field
     * @return DTO representing the updated form template
     * @throws ResponseStatusException if the template is not found or there's an error updating the template
     */
    @Operation(summary = "Add a field to a template", description = "Adds a field to a form template")
    @Transactional
    public FormDTO addFieldToTemplate(String templateName, FormFieldCreateDTO newField) {
        if (!isValidTemplateName(templateName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Template not found: " + templateName);
        }
        try {
            FormTemplateDTO template = templateLoader.loadTemplate(templateName);

            if (template.getFields().stream().anyMatch(field -> field.getQuestion().equals(newField.getQuestion()))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field with this question already exists");
            }

            template.getFields().add(newField);
            templateLoader.saveTemplate(templateName, template);

            List<Form> existingForms = formRepository.findByType(templateName);
            for (Form form : existingForms) {
                addFieldToForm(form, newField);
            }

            return createFormFromTemplate(templateName);
        } catch (IOException e) {
            logger.error("Error loading or saving template: {}", templateName, e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error loading or saving template: " + templateName);
        }
    }

    /**
     * Adds a field to a form.
     *
     * @param formId   ID of the form to add the field to
     * @param newField DTO containing the data for creating the new field
     * @return DTO representing the updated form
     * @throws ResponseStatusException if the form is not found or there's an error updating the form
     */
    @Operation(summary = "Add a field to a form", description = "Adds a field to a form")
    @Transactional
    public FormDTO addFieldToForm(Long formId, FormFieldCreateDTO newField) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, FormMessages.FORM_ID_NOT_FOUND));

        return FormMapper.INSTANCE.toDTO(addFieldToForm(form, newField));
    }

    /**
     * Adds a field to a form.
     *
     * @param form     Form to add the field to
     * @param newField DTO containing the data for creating the new field
     * @return Form with the new field added
     * @throws ResponseStatusException if the form is not found or there's an error updating the form
     */
    @Operation(summary = "Add a field to a form", description = "Adds a field to a form")
    private Form addFieldToForm(Form form, FormFieldCreateDTO newField) {
        if (form == null || form.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormMessages.FORM_ID_NOT_FOUND);
        }

        FormField formField = new FormField();
        formField.setFieldType(newField.getFieldType());
        formField.setQuestion(newField.getQuestion());
        formField = formFieldRepository.save(formField);

        FormFieldAnswer answer = new FormFieldAnswer();
        answer.setFormField(formField);
        answer.setAnswer("");
        answer.setForm(form);
        form.getFormFieldAnswers().add(answer);

        return formRepository.save(form);
    }

    /**
     * Retrieves a form template by its name.
     *
     * @param templateName Name of the template to retrieve
     * @return DTO representing the retrieved template
     * @throws ResponseStatusException if the template is not found
     */
    @Operation(summary = "Get a form template", description = "Retrieves a form template by its name")
    public FormTemplateDTO getTemplate(String templateName) {
        try {
            return templateLoader.loadTemplate(templateName);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, FormMessages.FORM_ID_NOT_FOUND + ": " + templateName);
        }
    }

    /**
     * Removes a field from a form template.
     *
     * @param templateName     Name of the template to modify
     * @param questionToRemove Question of the field to remove
     * @return DTO representing the updated form template
     * @throws ResponseStatusException if the template or field is not found, or if there's an error updating the template
     */
    @Operation(summary = "Remove a field from a template", description = "Removes a field from a form template and updates all related forms")
    @Transactional
    public FormDTO removeFieldFromTemplate(String templateName, String questionToRemove) {
        try {
            if (!isValidTemplateName(templateName)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormMessages.FORM_ID_NOT_FOUND + ": " + templateName);
            }

            FormTemplateDTO template = templateLoader.loadTemplate(templateName);

            boolean questionExists = template.getFields().stream()
                    .anyMatch(field -> field.getQuestion().equals(questionToRemove));
            if (!questionExists) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormMessages.QUESTION_NOT_FOUND + ": " + questionToRemove);
            }

            FormField fieldToInactivate = formFieldRepository.findByQuestion(questionToRemove)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, FormFieldMessages.FIELD_NOT_FOUND + ": " + questionToRemove));

            fieldToInactivate.setActive(false);
            formFieldRepository.save(fieldToInactivate);

            template.setFields(template.getFields().stream()
                    .filter(field -> !field.getQuestion().equals(questionToRemove))
                    .collect(Collectors.toList()));
            templateLoader.saveTemplate(templateName, template);

            List<Form> existingForms = formRepository.findByType(templateName);
            for (Form form : existingForms) {
                form.getFormFieldAnswers().stream()
                        .filter(answer -> answer.getFormField().getQuestion().equals(questionToRemove))
                        .forEach(answer -> answer.getFormField().setActive(false));
                formRepository.save(form);
            }

            return createFormFromTemplate(templateName);
        } catch (IOException e) {
            logger.error(FormMessages.ERROR_IN_REMOVE_FIELD_FROM_TEMPLATE, e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FormMessages.ERROR_IN_REMOVE_FIELD_FROM_TEMPLATE + ": " + templateName);
        }
    }

    /**
     * Deletes all forms.
     */
    @Operation(summary = "Delete all forms", description = "Deletes all forms")
    public void deleteAllForms() {
        formRepository.deleteAll();
    }

    /**
     * Deletes a form by its ID.
     *
     * @param formId ID of the form to delete
     * @return DTO representing the deleted form
     * @throws ResponseStatusException if the form is not found
     */
    @Operation(summary = "Delete a form", description = "Deletes a form by its ID")
    public FormDTO deleteForm(Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, FormMessages.FORM_ID_NOT_FOUND));

        formRepository.delete(form);
        return FormMapper.INSTANCE.toDTO(form);
    }

    /**
     * Retrieves all forms.
     *
     * @return List of DTOs representing all forms
     */
    @Operation(summary = "Get all forms", description = "Retrieves all forms")
    public List<FormDTO> getAllForms() {
        List<Form> forms = formRepository.findAll();
        return FormMapper.INSTANCE.toDTOList(forms);
    }
}