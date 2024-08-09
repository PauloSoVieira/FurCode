package org.mindera.fur.code.service.formTESTE;

import jakarta.transaction.Transactional;
import org.mindera.fur.code.controller.formTESTE.TemplateLoaderUtil;
import org.mindera.fur.code.dto.formTESTEDTO.*;
import org.mindera.fur.code.mapper.formMapper.FormFieldMapper;
import org.mindera.fur.code.mapper.formMapper.FormMapper;
import org.mindera.fur.code.model.formTest.Form;
import org.mindera.fur.code.model.formTest.FormField;
import org.mindera.fur.code.model.formTest.FormFieldAnswer;
import org.mindera.fur.code.model.formTest.FormTemplate;
import org.mindera.fur.code.repository.formTest.FormFieldAnswerRepository;
import org.mindera.fur.code.repository.formTest.FormFieldRepository;
import org.mindera.fur.code.repository.formTest.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    private final FormTemplateService formTemplateService;



    @Autowired
    public FormService(FormRepository formRepository, FormFieldRepository formFieldRepository, FormFieldAnswerRepository formFieldAnswerRepository, FormFieldService formFieldService, TemplateLoaderUtil templateLoader, FormTemplateService formTemplateService) {
        this.formRepository = formRepository;
        this.formFieldRepository = formFieldRepository;
        this.formFieldAnswerRepository = formFieldAnswerRepository;
        this.formFieldService = formFieldService;
        this.templateLoader = templateLoader;

        this.formTemplateService = formTemplateService;
    }

    public FormDTO createForm(String name) {
        Form form = new Form();
        form.setName(name.replaceAll("^\"|\"$", ""));
        form.setCreatedAt(LocalDateTime.now());
        form.setType("DEFAULT"); // TODO: Add a default type
        Form savedForm = formRepository.save(form);
        return FormMapper.INSTANCE.toDTO(savedForm);
    }

    public FormDTO getForm(Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found"));
        return FormMapper.INSTANCE.toDTO(form);
    }

//    public FormDTO createForm(String name, String type) {
//        Form form = new Form();
//        form.setName(name);
//        form.setType(type);
//        form.setCreatedAt(LocalDateTime.now());
//        Form savedForm = formRepository.save(form);
//        return FormMapper.INSTANCE.toDTO(savedForm);
//    }

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

//    public List<FormDTO> getFormsByType(String type) {
//        List<Form> forms = formRepository.findByType(type);
//        return FormMapper.INSTANCE.toDTOList(forms);
//    }

    public FormFieldDTO createFormField(FormFieldCreateDTO createDTO) {
        return formFieldService.createFormField(createDTO);
    }


        public List<FormFieldDTO> getFieldsForForm(Long formId) {
            Form form = formRepository.findById(formId)
                    .orElseThrow(() -> new RuntimeException("Form not found"));

            List<FormField> formFields = form.getFormFieldAnswers().stream()
                    .map(FormFieldAnswer::getFormField)
                    .collect(Collectors.toList());

            return FormFieldMapper.INSTANCE.toDTOList(formFields);
    }



//
//    public FormDTO createFormFromTemplate(String name, Long templateId) {
//        FormTemplate template = formTemplateService.getFormTemplate(templateId);
//
//        Form form = new Form();
//        form.setName(name);
//        form.setCreatedAt(LocalDateTime.now());
//        form.setType("TEMPLATE_BASED");
//        form.setFormTemplate(template);
//
//        for (FormField field : template.getFormFields()) {
//            FormFieldAnswer answer = new FormFieldAnswer();
//            answer.setFormField(field);
//            answer.setAnswer(""); // Empty answer to be filled by user
//            form.addFormFieldAnswer(answer);
//        }
//
//        Form savedForm = formRepository.save(form);
//        return FormMapper.INSTANCE.toDTO(savedForm);
//    }


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

        Map<Long, FormFieldAnswer> answerMap = form.getFormFieldAnswers().stream()
                .collect(Collectors.toMap(a -> a.getFormField().getId(), a -> a));

        for (FieldAnswerDTO fieldAnswer : formAnswerDTO.getAnswers()) {
            FormFieldAnswer answer = answerMap.get(fieldAnswer.getFieldId());
            if (answer == null) {
                throw new RuntimeException("Field not found in form");
            }
            answer.setAnswer(fieldAnswer.getAnswer());
            formFieldAnswerRepository.save(answer);
        }

        return FormMapper.INSTANCE.toDTO(form);
    }

    @Transactional
    public FormDTO addFieldToTemplate(String templateName, FormFieldCreateDTO newField) throws IOException {
        FormTemplateDTO template = templateLoader.loadTemplate(templateName);
        template.getFields().add(newField);
        templateLoader.saveTemplate(templateName, template);

        // Update all existing forms based on this template
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

    public FormTemplateDTO getTemplate(Long templateId) throws IOException {
        return templateLoader.loadTemplate(String.valueOf(templateId));
    }

}