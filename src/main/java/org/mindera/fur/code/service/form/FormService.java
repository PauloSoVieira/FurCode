package org.mindera.fur.code.service.form;

import jakarta.transaction.Transactional;
import org.mindera.fur.code.controller.form.TemplateLoaderUtil;
import org.mindera.fur.code.dto.form.*;
import org.mindera.fur.code.mapper.formMapper.FormMapper;
import org.mindera.fur.code.model.form.Form;
import org.mindera.fur.code.model.form.FormField;
import org.mindera.fur.code.model.form.FormFieldAnswer;
import org.mindera.fur.code.repository.form.FormFieldAnswerRepository;
import org.mindera.fur.code.repository.form.FormFieldRepository;
import org.mindera.fur.code.repository.form.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class FormService {
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

//        this.formTemplateService = formTemplateService;
    }

    public FormDTO createForm(FormCreateDTO formCreateDTO) {

        Form form = FormMapper.INSTANCE.toModelFromCreateDTO(formCreateDTO);
        Form savedForm = formRepository.save(form);
        return FormMapper.INSTANCE.toDTO(savedForm);
    }
    public FormDTO getForm(Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found"));
        return FormMapper.INSTANCE.toDTO(form);
    }


    public FormDTO addFieldToForm(Long formId, Long fieldId, String answer) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found"));
        FormField formField = formFieldRepository.findById(fieldId)
                .orElseThrow(() -> new RuntimeException("FormField not found"));

        FormFieldAnswer formFieldAnswer = new FormFieldAnswer();
        formFieldAnswer.setFormField(formField);
        formFieldAnswer.setAnswer(answer);

        form.addFormFieldAnswer(formFieldAnswer);
        Form savedForm = formRepository.save(form);

        return FormMapper.INSTANCE.toDTO(savedForm);
    }








    @Transactional
    public FormDTO createFormFromTemplate(String templateName) throws IOException {
        FormTemplateDTO template = templateLoader.loadTemplate(templateName);

        // Check if template fields already exist
        List<FormField> existingFields = formFieldRepository.findByQuestionIn(
                template.getFields().stream().map(FormFieldCreateDTO::getQuestion).collect(Collectors.toList())
        );

        Map<String, FormField> existingFieldMap = existingFields.stream()
                .collect(Collectors.toMap(FormField::getQuestion, f -> f));

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
    }

    @Transactional
    public FormDTO submitFormAnswers(FormAnswerDTO formAnswerDTO) {
        Form form = formRepository.findById(formAnswerDTO.getFormId())
                .orElseThrow(() -> new RuntimeException("Form not found"));

        if ("DONATION_TEMPLATE".equals(form.getType())) {
            validateDonationForm(formAnswerDTO);
        }

        Map<Long, FormFieldAnswer> answerMap = form.getFormFieldAnswers().stream()
                .collect(Collectors.toMap(a -> a.getFormField().getId(), a -> a));

        for (FieldAnswerDTO fieldAnswer : formAnswerDTO.getAnswers()) {
            FormFieldAnswer answer = answerMap.get(fieldAnswer.getFieldId());
            if (answer == null) {
                throw new RuntimeException("Field not found in form: " + fieldAnswer.getFieldId());
            }
            answer.setAnswer(fieldAnswer.getAnswer());
        }

        Form savedForm = formRepository.save(form);
        return FormMapper.INSTANCE.toDTO(savedForm);
    }

    private void validateDonationForm(FormAnswerDTO formAnswerDTO) {
        for (FieldAnswerDTO fieldAnswer : formAnswerDTO.getAnswers()) {
            FormField field = formFieldRepository.findById(fieldAnswer.getFieldId())
                    .orElseThrow(() -> new RuntimeException("Field not found: " + fieldAnswer.getFieldId()));

            if ("Donation Amount ($)".equals(field.getQuestion())) {
                try {
                    BigDecimal amount = new BigDecimal(fieldAnswer.getAnswer());
                    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new IllegalArgumentException("Donation amount must be positive");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid donation amount");
                }
            }
        }
    }


    @Transactional
    public FormDTO addFieldToTemplate(String templateName, FormFieldCreateDTO newField) throws IOException {
        FormTemplateDTO template = templateLoader.loadTemplate(templateName);
        template.getFields().add(newField);
        templateLoader.saveTemplate(templateName, template);

        List<Form> existingForms = formRepository.findByType(templateName);
        for (Form form : existingForms) {
            addFieldToForm(form, newField);
        }

        return createFormFromTemplate(templateName);
    }

    @Transactional
    public FormDTO addFieldToForm(Long formId, FormFieldCreateDTO newField) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found"));

        return FormMapper.INSTANCE.toDTO(addFieldToForm(form, newField));
    }

    private Form addFieldToForm(Form form, FormFieldCreateDTO newField) {
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


    public FormTemplateDTO getTemplate(String templateName) throws IOException {
        return templateLoader.loadTemplate(templateName);
    }



    @Transactional
    public FormDTO removeFieldFromForm(Long formId, Long fieldId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found"));

        FormFieldAnswer fieldAnswerToRemove = form.getFormFieldAnswers().stream()
                .filter(answer -> answer.getFormField().getId().equals(fieldId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Field not found in form"));

        form.getFormFieldAnswers().remove(fieldAnswerToRemove);
        formFieldAnswerRepository.delete(fieldAnswerToRemove);

        Form updatedForm = formRepository.save(form);
        return FormMapper.INSTANCE.toDTO(updatedForm);
    }


    public void deleteAllForms() {
        formRepository.deleteAll();
    }
}