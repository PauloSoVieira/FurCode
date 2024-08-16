package org.mindera.fur.code.controller.form;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.mindera.fur.code.dto.form.*;
import org.mindera.fur.code.service.form.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "Form Management", description = "APIs for managing forms")
@RestController
@RequestMapping("/api/v1/forms")
@Tag(name = "Form Management", description = "APIs for managing forms")
public class FormController {
    private final FormService formService;
    private final TemplateLoaderUtil templateLoaderUtil;


    /**
     * Constructor for FormController
     *
     * @param formService
     */
    @Autowired
    public FormController(FormService formService, TemplateLoaderUtil templateLoaderUtil) {
        this.formService = formService;
        this.templateLoaderUtil = templateLoaderUtil;
    }

    /**
     * Creates a new form based on the provided data
     *
     * @param formCreateDTO
     * @return
     */
    @Schema(name = "Create a new form", description = "Creates a new form based on the provided data")
    @PostMapping
    @Operation(summary = "Create a new form", description = "Creates a new form based on the provided data")
    public ResponseEntity<FormDTO> createForm(@RequestBody FormCreateDTO formCreateDTO) {
        return new ResponseEntity<>(formService.createForm(formCreateDTO), HttpStatus.CREATED);
    }

    /**
     * Creates a new form based on a predefined template
     *
     * @param templateName
     * @return
     */
    @Schema(name = "Create a form from a predefined template", description = "Creates a new form based on a predefined template")
    @PostMapping("/template/{templateName}")
    @Operation(summary = "Create a form from a predefined template", description = "Creates a new form based on a predefined template")
    public ResponseEntity<FormDTO> createFormFromTemplate(@PathVariable String templateName) {
        return new ResponseEntity<>(formService.createFormFromTemplate(templateName), HttpStatus.CREATED);
    }

    /**
     * Submit answers for a specific form
     *
     * @param formId
     * @param formDTO
     * @return
     */
    @Schema(name = "Submit answers for a form", description = "Submits answers for a specific form")
    @PostMapping("/submit/{formId}")
    @Operation(summary = "Submit answers for a form", description = "Submits answers for a specific form")
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

    /**
     * Adds a new field to a template and all its forms
     *
     * @param templateName
     * @param newField
     * @return
     */
    @Schema(name = "Add a new field to a template and all its forms", description = "Adds a new field to a template and all its forms")
    @PostMapping("/template/{templateName}/field")
    @Operation(summary = "Add a new field to a template and all its forms", description = "Adds a new field to a template and all its forms")
    public ResponseEntity<FormDTO> addFieldToTemplate(@PathVariable String templateName, @RequestBody FormFieldCreateDTO newField) {

        return new ResponseEntity<>(formService.addFieldToTemplate(templateName, newField), HttpStatus.CREATED);
    }

    /**
     * Adds a new field to a specific form
     *
     * @param formId
     * @param newField
     * @return
     */
    @Schema(name = "Add a new field to a specific form", description = "Adds a new field to a specific form")
    @PostMapping("/{formId}/field")
    @Operation(summary = "Add a new field to a specific form", description = "Adds a new field to a specific form")
    public ResponseEntity<FormDTO> addFieldToForm(@PathVariable Long formId, @RequestBody FormFieldCreateDTO newField) {
        return new ResponseEntity<>(formService.addFieldToForm(formId, newField), HttpStatus.CREATED);
    }

    /**
     * Gets a template by name
     *
     * @param templateName
     * @return
     */
    @Schema(name = "Get a template", description = "Gets a template by name")
    @GetMapping("/template/{templateName}")
    @Operation(summary = "Get a template", description = "Gets a template by name")
    public ResponseEntity<FormTemplateDTO> getTemplate(@PathVariable String templateName) {
        return new ResponseEntity<>(formService.getTemplate(templateName), HttpStatus.OK);
    }

    /**
     * Gets a form by id
     *
     * @param formId
     * @return
     */
    @Schema(name = "Get a form", description = "Gets a form by id")
    @GetMapping("/{formId}")
    @Operation(summary = "Get a form", description = "Gets a form by id")
    public ResponseEntity<FormDTO> getForm(@PathVariable Long formId) {
        return new ResponseEntity<>(formService.getForm(formId), HttpStatus.OK);
    }

    /**
     * Removes a field from a template
     *
     * @param templateName
     * @param question
     * @return
     * @throws IOException
     */
    @Schema(name = "Remove a field from a template", description = "Removes a field from a template")
    @DeleteMapping("/template/{templateName}/field")
    @Operation(summary = "Remove a field from a template", description = "Removes a field from a template")
    public ResponseEntity<FormDTO> removeFieldFromTemplate(
            @PathVariable String templateName,
            @RequestParam String question) throws IOException {
        return ResponseEntity.ok(formService.removeFieldFromTemplate(templateName, question));
    }

    /**
     * Deletes a form by id
     *
     * @param formId
     * @return
     */
    @Schema(name = "Delete a form", description = "Deletes a form by id")
    @DeleteMapping("/{formId}")
    @Operation(summary = "Delete a form", description = "Deletes a form by id")
    public ResponseEntity<FormDTO> deleteForm(@PathVariable Long formId) {
        return new ResponseEntity<>(formService.deleteForm(formId), HttpStatus.NO_CONTENT);
    }

    /**
     * Gets all forms
     *
     * @return
     */
    @Schema(name = "Get all forms", description = "Gets all forms")
    @GetMapping("/all")
    @Operation(summary = "Get all forms", description = "Gets all forms")
    public ResponseEntity<List<FormDTO>> getAllForms() {
        return new ResponseEntity<>(formService.getAllForms(), HttpStatus.OK);
    }

    @Schema(name = "Get all template names", description = "Retrieves all available template names")
    @GetMapping("/templates")
    @Operation(summary = "Get all template names", description = "Retrieves all available template names")
    public ResponseEntity<List<String>> getAllTemplateNames() throws IOException {
        return new ResponseEntity<>(templateLoaderUtil.getAllTemplateNames(), HttpStatus.OK);
    }

}