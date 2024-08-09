package org.mindera.fur.code.controller.formTESTE;

import io.swagger.v3.oas.annotations.Operation;
import org.mindera.fur.code.dto.formTESTEDTO.*;
import org.mindera.fur.code.service.formTESTE.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/forms")
public class FormController {
    private final FormService formService;

    @Autowired
    public FormController(FormService formService) {
        this.formService = formService;
    }

    @PostMapping
    public ResponseEntity<FormDTO> createForm(@RequestBody String name) {
        return ResponseEntity.ok(formService.createForm(name));
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
        return ResponseEntity.ok(formService.createFormFromTemplate(templateName));
    }

    @PostMapping("/{formId}/submit")
    @Operation(summary = "Submit answers for a form")
    public ResponseEntity<FormDTO> submitFormAnswers(@PathVariable Long formId, @RequestBody FormAnswerDTO formAnswerDTO) {
        formAnswerDTO.setFormId(formId);
        return ResponseEntity.ok(formService.submitFormAnswers(formAnswerDTO));
    }


    @PostMapping("/template/{templateName}/field")
    @Operation(summary = "Add a new field to a template and all its forms")
    public ResponseEntity<FormDTO> addFieldToTemplate(@PathVariable String templateName, @RequestBody FormFieldCreateDTO newField) throws IOException {
        return ResponseEntity.ok(formService.addFieldToTemplate(templateName, newField));
    }

    @PostMapping("/{formId}/field")
    @Operation(summary = "Add a new field to a specific form")
    public ResponseEntity<FormDTO> addFieldToForm(@PathVariable Long formId, @RequestBody FormFieldCreateDTO newField) {
        return ResponseEntity.ok(formService.addFieldToForm(formId, newField));
    }


    @GetMapping("/template/{templateName}")
    @Operation(summary = "Get a template")
    public ResponseEntity<FormTemplateDTO> getTemplate(@PathVariable String templateName) throws IOException {
        return ResponseEntity.ok(formService.getTemplate(templateName));
    }

    @GetMapping("/{templateId}")
    @Operation(summary = "Get a template")
    public ResponseEntity<FormTemplateDTO> getTemplate(@PathVariable Long templateId) throws IOException {
        return ResponseEntity.ok(formService.getTemplate(templateId));
    }

    @GetMapping("/forms/{formId}")
    @Operation(summary = "Get a form")
    public ResponseEntity<FormDTO> getForm(@PathVariable Long formId) {
        return ResponseEntity.ok(formService.getForm(formId));
    }
}