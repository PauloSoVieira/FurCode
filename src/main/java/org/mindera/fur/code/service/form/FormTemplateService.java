package org.mindera.fur.code.service.form;

import org.mindera.fur.code.model.form.FormField;
import org.mindera.fur.code.model.form.FormTemplate;
import org.mindera.fur.code.repository.formTest.FormFieldRepository;
import org.mindera.fur.code.repository.formTest.FormTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormTemplateService {

    private final FormTemplateRepository formTemplateRepository;
    private final FormFieldRepository formFieldRepository;

    @Autowired
    public FormTemplateService(FormTemplateRepository formTemplateRepository, FormFieldRepository formFieldRepository) {
        this.formTemplateRepository = formTemplateRepository;
        this.formFieldRepository = formFieldRepository;
    }

    public FormTemplate createFormTemplate(String name, List<Long> formFieldIds) {
        FormTemplate formTemplate = new FormTemplate();
        formTemplate.setName(name);

        List<FormField> formFields = formFieldRepository.findAllById(formFieldIds);
        formTemplate.setFormFields(formFields);

        return formTemplateRepository.save(formTemplate);
    }

    public FormTemplate getFormTemplate(Long id) {
        return formTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FormTemplate not found"));
    }

    public List<FormTemplate> getAllFormTemplates() {
        return formTemplateRepository.findAll();
    }
}