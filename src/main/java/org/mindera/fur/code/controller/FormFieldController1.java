package org.mindera.fur.code.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.mindera.fur.code.dto.forms.FormFieldCreateDTO1;
import org.mindera.fur.code.dto.forms.FormFieldDTO1;
import org.mindera.fur.code.service.FormFieldService2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing form fields.
 */
@RestController
@RequestMapping("/api/v1/field")
public class FormFieldController1 {


    private FormFieldService2 formFieldService2;

    /**
     * Constructor for dependency injection.
     *
     * @param formFieldService2 the service for managing form fields.
     */
    public FormFieldController1(FormFieldService2 formFieldService2) {
        this.formFieldService2 = formFieldService2;
    }

    /**
     * Retrieves all form fields.
     *
     * @return a list of FormFieldDTOs.
     */
    @Operation(summary = "Get all form fields", description = "Retrieve a list of all form fields.")
    @GetMapping("/all")
    public ResponseEntity<List<FormFieldDTO1>> getAllFields() {
        return new ResponseEntity<>(formFieldService2.getAll(), HttpStatus.OK);
    }


    /**
     * Retrieves a form field by ID.
     *
     * @param id the ID of the form field.
     * @return the FormFieldDTO.
     */
    @Operation(summary = "Get form field by ID", description = "Retrieve a form field by ID.")
    @GetMapping("/{id}")
    public ResponseEntity<FormFieldDTO1> getFieldById(@PathVariable Long id) {
        return new ResponseEntity<>(formFieldService2.getById(id), HttpStatus.OK);
    }

    /**
     * Creates a new form field.
     *
     * @param formFieldDTO the FormFieldCreateDTO.
     * @return the created FormFieldDTO.
     */
    @Operation(summary = "Create a new form field", description = "Create a new form field.")
    @PostMapping
    public ResponseEntity<FormFieldDTO1> createField(@RequestBody FormFieldCreateDTO1 formFieldDTO) {
        return new ResponseEntity<>(formFieldService2.createField(formFieldDTO), HttpStatus.OK);
    }

    /**
     * Updates an existing form field.
     *
     * @param id           the ID of the form field to be updated.
     * @param formFieldDTO1 the FormFieldDTO.
     * @return the updated FormFieldDTO.
     */
    @Operation(summary = "Update an existing form field", description = "Update an existing form field.")
    @PutMapping("update/{id}")
    public ResponseEntity<FormFieldDTO1> updateField(@PathVariable Long id, @RequestBody FormFieldDTO1 formFieldDTO1) {
        return new ResponseEntity<>(formFieldService2.updateField(id, formFieldDTO1), HttpStatus.OK);
    }

    /**
     * Deletes a form field by ID.
     *
     * @param id the ID of the form field to be deleted.
     * @return the deleted FormFieldDTO.
     */
    @Operation(summary = "Delete a form field by ID", description = "Delete a form field by ID.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<FormFieldDTO1> deleteField(@PathVariable Long id) {
        return new ResponseEntity<>(formFieldService2.deleteField(id), HttpStatus.NO_CONTENT);
    }

}
