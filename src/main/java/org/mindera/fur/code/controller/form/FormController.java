package org.mindera.fur.code.controller.form;

import io.swagger.v3.oas.annotations.Operation;
import org.mindera.fur.code.dto.form.*;
import org.mindera.fur.code.service.form.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/forms")
public class FormController {
    private final FormService formService;

    @Autowired
    public FormController(FormService formService) {
        this.formService = formService;
    }

    @PostMapping
    public ResponseEntity<FormDTO> createForm(@RequestBody FormCreateDTO formCreateDTO) {
        return new ResponseEntity<>(formService.createForm(formCreateDTO), HttpStatus.CREATED);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<FormDTO> getForm(@PathVariable Long id) {
//        return ResponseEntity.ok(formService.getForm(id));
//    }
//
//    @PostMapping("/{formId}/fields/{fieldId}")
//    @Operation(summary = "Add a field to a form with an answer")
//    public ResponseEntity<FormDTO> addFieldToForm(
//            @PathVariable Long formId,
//            @PathVariable Long fieldId,
//            @RequestBody FieldAnswerDTO answerDTO) {
//        return ResponseEntity.ok(formService.addFieldToForm(formId, fieldId, answerDTO.getAnswer()));
//    }
//
//    @GetMapping("/{formId}/fields")
//    @Operation(summary = "Get all fields for a form")
//    public ResponseEntity<List<FormFieldDTO>> getFieldsForForm(@PathVariable Long formId) {
//        return ResponseEntity.ok(formService.getFieldsForForm(formId));
//    }


    @PostMapping("/template/{templateName}")
    @Operation(summary = "Create a form from a predefined template")
    public ResponseEntity<FormDTO> createFormFromTemplate(@PathVariable String templateName) throws IOException {
        // return ResponseEntity.ok(formService.createFormFromTemplate(templateName));
        return new ResponseEntity<>(formService.createFormFromTemplate(templateName), HttpStatus.CREATED);
    }

    @PostMapping("/{formId}")
    @Operation(summary = "Submit answers for a form")
    public ResponseEntity<FormDTO> submitFormAnswers(@PathVariable Long formId, @RequestBody FormDTO formDTO) {
        FormAnswerDTO formAnswerDTO = new FormAnswerDTO();
        formAnswerDTO.setFormId(formId);
        List<FieldAnswerDTO> answers = formDTO.getFormFieldAnswers().stream()
                .map(ffa -> {
                    FieldAnswerDTO fieldAnswer = new FieldAnswerDTO();
                    fieldAnswer.setFieldId(ffa.getFormFieldId());
                    fieldAnswer.setAnswer(ffa.getAnswer());
                    return fieldAnswer;
                })
                .collect(Collectors.toList());
        formAnswerDTO.setAnswers(answers);

        return new ResponseEntity<>(formService.submitFormAnswers(formAnswerDTO), HttpStatus.CREATED);
    }


    @PostMapping("/template/{templateName}/field")
    @Operation(summary = "Add a new field to a template and all its forms")
    public ResponseEntity<FormDTO> addFieldToTemplate(@PathVariable String templateName, @RequestBody FormFieldCreateDTO newField) throws IOException {

        return new ResponseEntity<>(formService.addFieldToTemplate(templateName, newField), HttpStatus.CREATED);
    }

    @PostMapping("/{formId}/field")
    @Operation(summary = "Add a new field to a specific form")
    public ResponseEntity<FormDTO> addFieldToForm(@PathVariable Long formId, @RequestBody FormFieldCreateDTO newField) {
        return new ResponseEntity<>(formService.addFieldToForm(formId, newField), HttpStatus.CREATED);
    }


    @GetMapping("/template/{templateName}")
    @Operation(summary = "Get a template")
    public ResponseEntity<FormTemplateDTO> getTemplate(@PathVariable String templateName) throws IOException {
        return new ResponseEntity<>(formService.getTemplate(templateName), HttpStatus.OK);
    }


    @GetMapping("/{formId}")
    @Operation(summary = "Get a form")
    public ResponseEntity<FormDTO> getForm(@PathVariable Long formId) {
        return new ResponseEntity<>(formService.getForm(formId), HttpStatus.OK);
    }

    @DeleteMapping("/template/{templateName}/field")
    @Operation(summary = "Remove a field from a template")
    public ResponseEntity<FormDTO> removeFieldFromTemplate(
            @PathVariable String templateName,
            @RequestParam String question) throws IOException {
        return ResponseEntity.ok(formService.removeFieldFromTemplate(templateName, question));
    }

    @DeleteMapping("/{formId}")
    @Operation(summary = "Delete a form")
    public ResponseEntity<FormDTO> deleteForm(@PathVariable Long formId) {
        return new ResponseEntity<>(formService.deleteForm(formId), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all forms")
    public ResponseEntity<List<FormDTO>> getAllForms() {
        return new ResponseEntity<>(formService.getAllForms(), HttpStatus.OK);
    }
}