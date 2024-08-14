package org.mindera.fur.code.service.form;

import jakarta.transaction.Transactional;
import org.mindera.fur.code.controller.form.TemplateLoaderUtil;
import org.mindera.fur.code.dto.form.*;
import org.mindera.fur.code.exceptions.adoptionFormException.AdoptionFormNotFound;
import org.mindera.fur.code.mapper.formMapper.FormMapper;
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

@Service

public class FormService {
    private static final Logger logger = LoggerFactory.getLogger(FormService.class);

    private final FormRepository formRepository;
    private final FormFieldRepository formFieldRepository;
    private final FormFieldAnswerRepository formFieldAnswerRepository;
    private final FormFieldService formFieldService;
    private final TemplateLoaderUtil templateLoader;


    @Autowired
    public FormService(FormRepository formRepository, FormFieldRepository formFieldRepository, FormFieldAnswerRepository formFieldAnswerRepository, FormFieldService formFieldService, TemplateLoaderUtil templateLoader) {
        this.formRepository = formRepository;
        this.formFieldRepository = formFieldRepository;
        this.formFieldAnswerRepository = formFieldAnswerRepository;
        this.formFieldService = formFieldService;
        this.templateLoader = templateLoader;
    }

    @Transactional
    public FormDTO createForm(FormCreateDTO formCreateDTO) {
        if (formCreateDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Form not found");
        }
        Form form = new Form();
        form.setName(formCreateDTO.getName());
        form.setType(formCreateDTO.getType());
        form.setCreatedAt(formCreateDTO.getCreatedAt() != null ? formCreateDTO.getCreatedAt() : LocalDateTime.now());

        List<FormFieldAnswer> formFieldAnswers = new ArrayList<>();
        for (FormFieldAnswerDTO answerDTO : formCreateDTO.getFormFieldAnswers()) {
            FormField formField = new FormField();
            formField.setFieldType(answerDTO.getAnswer());
            formField.setQuestion(answerDTO.getQuestion());
            formField.setForm(form);

            FormFieldAnswer answer = new FormFieldAnswer();
            answer.setFormField(formField);
            answer.setAnswer(answerDTO.getAnswer());
            answer.setForm(form);
            formFieldAnswers.add(answer);
        }

        form.setFormFieldAnswers(formFieldAnswers);
        form.setFields(formFieldAnswers.stream().map(FormFieldAnswer::getFormField).collect(Collectors.toList()));

        Form savedForm = formRepository.save(form);
        return FormMapper.INSTANCE.toDTO(savedForm);
    }

    public FormDTO getForm(Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Form not found"));
        return FormMapper.INSTANCE.toDTO(form);
    }


    private boolean isValidTemplateName(String templateName) {

        List<String> validTemplateNames = Arrays.asList("adoption-template", "donation-template");
        return validTemplateNames.contains(templateName);
    }

    @Transactional
    public FormDTO createFormFromTemplate(String templateName) {
        if (!isValidTemplateName(templateName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teste: " + templateName);
        }

        try {
            FormTemplateDTO template = templateLoader.loadTemplate(templateName);

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
        } catch (IOException e) {
            logger.error("Error loading template: {}", templateName, e);
            throw new AdoptionFormNotFound("Error loading template: " + templateName);
        } catch (Exception e) {
            logger.error("Error creating form from template: {}", templateName, e);
            throw new AdoptionFormNotFound("Error creating form from template: " + templateName);
        }
    }

    @Transactional
    public FormDTO submitFormAnswers(FormAnswerDTO formAnswerDTO) {
        Form form = formRepository.findById(formAnswerDTO.getFormId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Form not found"));

        Map<Long, FormFieldAnswer> answerMap = form.getFormFieldAnswers().stream()
                .collect(Collectors.toMap(answer -> answer.getFormField().getId(), answer -> answer));

        for (FieldAnswerDTO fieldAnswer : formAnswerDTO.getAnswers()) {
            FormFieldAnswer answer = answerMap.get(fieldAnswer.getFieldId());
            if (answer == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Field not found: " + fieldAnswer.getFieldId());
            }
            answer.setAnswer(fieldAnswer.getAnswer());
        }

        Form savedForm = formRepository.save(form);
        return FormMapper.INSTANCE.toDTO(savedForm);
    }


    @Transactional
    public FormDTO addFieldToTemplate(String templateName, FormFieldCreateDTO newField) {
        if (!isValidTemplateName(templateName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Template not found: " + templateName);
        }
        try {
            FormTemplateDTO template = templateLoader.loadTemplate(templateName);
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

    @Transactional
    public FormDTO addFieldToForm(Long formId, FormFieldCreateDTO newField) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Form not found"));

        return FormMapper.INSTANCE.toDTO(addFieldToForm(form, newField));
    }

    private Form addFieldToForm(Form form, FormFieldCreateDTO newField) {
        if (form == null || form.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Form not found");
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


    public FormTemplateDTO getTemplate(String templateName) {
        try {
            return templateLoader.loadTemplate(templateName);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Template not found: " + templateName);
        }
    }


    @Transactional
    public FormDTO removeFieldFromTemplate(String templateName, String questionToRemove) {
        try {
            if (!isValidTemplateName(templateName)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Template not found: " + templateName);
            }

            FormTemplateDTO template = templateLoader.loadTemplate(templateName);

            boolean questionExists = template.getFields().stream()
                    .anyMatch(field -> field.getQuestion().equals(questionToRemove));
            if (!questionExists) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question not found in template: " + questionToRemove);
            }

            FormField fieldToInactivate = formFieldRepository.findByQuestion(questionToRemove)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field not found: " + questionToRemove));

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
            logger.error("Error in removeFieldFromTemplate: ", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in removeFieldFromTemplate: " + templateName);
        }
    }

    public void deleteAllForms() {
        formRepository.deleteAll();
    }

    public FormDTO deleteForm(Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Form not found"));

        formRepository.delete(form);
        return FormMapper.INSTANCE.toDTO(form);
    }

    public List<FormDTO> getAllForms() {
        List<Form> forms = formRepository.findAll();
        return FormMapper.INSTANCE.toDTOList(forms);
    }
}