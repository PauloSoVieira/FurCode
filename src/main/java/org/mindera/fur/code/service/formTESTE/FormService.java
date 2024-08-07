package org.mindera.fur.code.service.formTESTE;

import org.mindera.fur.code.dto.formTESTEDTO.FormDTO;
import org.mindera.fur.code.dto.formTESTEDTO.FormFieldCreateDTO;
import org.mindera.fur.code.dto.formTESTEDTO.FormFieldDTO;
import org.mindera.fur.code.mapper.formMapper.FormFieldMapper;
import org.mindera.fur.code.mapper.formMapper.FormMapper;
import org.mindera.fur.code.model.formTest.Form;
import org.mindera.fur.code.model.formTest.FormField;
import org.mindera.fur.code.model.formTest.FormFieldAnswer;
import org.mindera.fur.code.repository.formTest.FormFieldAnswerRepository;
import org.mindera.fur.code.repository.formTest.FormFieldRepository;
import org.mindera.fur.code.repository.formTest.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class FormService {
    private final FormRepository formRepository;
    private final FormFieldRepository formFieldRepository;
    private final FormFieldAnswerRepository formFieldAnswerRepository;
    private final FormFieldService formFieldService;

    @Autowired
    public FormService(FormRepository formRepository, FormFieldRepository formFieldRepository, FormFieldAnswerRepository formFieldAnswerRepository, FormFieldService formFieldService) {
        this.formRepository = formRepository;
        this.formFieldRepository = formFieldRepository;
        this.formFieldAnswerRepository = formFieldAnswerRepository;
        this.formFieldService = formFieldService;
    }

    public FormDTO createForm(String name) {
        Form form = new Form();
        form.setName(name);
        form = formRepository.save(form);
        return FormMapper.INSTANCE.toDTO(form);
    }

    public FormDTO getForm(Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found"));
        return FormMapper.INSTANCE.toDTO(form);
    }

    public FormDTO createForm(String name, String type) {
        Form form = new Form();
        form.setName(name);
        form.setType(type);
        form.setCreatedAt(LocalDateTime.now());
        Form savedForm = formRepository.save(form);
        return FormMapper.INSTANCE.toDTO(savedForm);
    }

    public FormDTO addFieldToForm(Long formId, Long fieldId, String answer) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found"));
        FormField formField = formFieldRepository.findById(fieldId)
                .orElseThrow(() -> new RuntimeException("FormField not found"));

        FormFieldAnswer formFieldAnswer = new FormFieldAnswer();
        formFieldAnswer.setForm(form);
        formFieldAnswer.setFormField(formField);
        formFieldAnswer.setAnswer(answer);

        form.getFormFieldAnswers().add(formFieldAnswer);
        form = formRepository.save(form);

        return FormMapper.INSTANCE.toDTO(form);
    }

    public List<FormDTO> getFormsByType(String type) {
        List<Form> forms = formRepository.findByType(type);
        return FormMapper.INSTANCE.toDTOList(forms);
    }

    public FormFieldDTO createFormField(FormFieldCreateDTO createDTO) {
        return formFieldService.createFormField(createDTO);
    }
}