package org.mindera.fur.code.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.mindera.fur.code.dto.forms.FormFieldDTO;
import org.mindera.fur.code.service.FormFieldService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing form fields.
 */
@RestController
@RequestMapping("/v1/api/field")
public class FormFieldController {


    private FormFieldService formFieldService;

    /**
     * Constructor for dependency injection.
     *
     * @param formFieldService the service for managing form fields.
     */
    public FormFieldController(FormFieldService formFieldService) {
        this.formFieldService = formFieldService;
    }

    /**
     * Retrieves all form fields.
     *
     * @return a list of FormFieldDTOs.
     */
    @Operation(summary = "Get all form fields", description = "Retrieve a list of all form fields.")
    @GetMapping("/all")
    public ResponseEntity<List<FormFieldDTO>> getAllFields() {
        return new ResponseEntity<>(formFieldService.getAll(), HttpStatus.OK);
    }


    /**
     * Retrieves a form field by ID.
     *
     * @param id the ID of the form field.
     * @return the FormFieldDTO.
     */
    @Operation(summary = "Get form field by ID", description = "Retrieve a form field by ID.")
    @GetMapping("/{id}")
    public ResponseEntity<FormFieldDTO> getFieldById(@PathVariable Long id) {
        return new ResponseEntity<>(formFieldService.getById(id), HttpStatus.OK);
    }

    /**
     * Creates a new form field.
     *
     * @param formFieldDTO the FormFieldDTO.
     * @return the created FormFieldDTO.
     */
    @Operation(summary = "Create a new form field", description = "Create a new form field.")
    @PostMapping
    public ResponseEntity<FormFieldDTO> getFieldById(@RequestBody FormFieldDTO formFieldDTO) {
        return new ResponseEntity<>(formFieldService.createField(formFieldDTO), HttpStatus.OK);
    }

    /**
     * Updates an existing form field.
     *
     * @param id           the ID of the form field to be updated.
     * @param formFieldDTO the FormFieldDTO.
     * @return the updated FormFieldDTO.
     */
    @Operation(summary = "Update an existing form field", description = "Update an existing form field.")
    @PutMapping("update/{id}")
    public ResponseEntity<FormFieldDTO> updateField(@PathVariable Long id, @RequestBody FormFieldDTO formFieldDTO) {
        return new ResponseEntity<>(formFieldService.updateField(id, formFieldDTO), HttpStatus.OK);
    }

    /**
     * Deletes a form field by ID.
     *
     * @param id the ID of the form field to be deleted.
     * @return the deleted FormFieldDTO.
     */
    @Operation(summary = "Delete a form field by ID", description = "Delete a form field by ID.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<FormFieldDTO> deleteField(@PathVariable Long id) {
        return new ResponseEntity<>(formFieldService.deleteField(id), HttpStatus.OK);
    }

}
