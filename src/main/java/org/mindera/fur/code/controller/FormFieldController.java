package org.mindera.fur.code.controller;


import org.mindera.fur.code.dto.formsDTO.FormFieldDTO;
import org.mindera.fur.code.service.FormFieldService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/field")
public class FormFieldController {


    private FormFieldService formFieldService;


    public FormFieldController(FormFieldService formFieldService) {
        this.formFieldService = formFieldService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<FormFieldDTO>> getAllFields() {
        return new ResponseEntity<>(formFieldService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormFieldDTO> getFieldById(@PathVariable Long id) {
        return new ResponseEntity<>(formFieldService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FormFieldDTO> getFieldById(@RequestBody FormFieldDTO formFieldDTO) {
        return new ResponseEntity<>(formFieldService.createField(formFieldDTO), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<FormFieldDTO> updateField(@PathVariable Long id, @RequestBody FormFieldDTO formFieldDTO) {
        return new ResponseEntity<>(formFieldService.updateField(id, formFieldDTO), HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<FormFieldDTO> deleteField(@PathVariable Long id) {
        return new ResponseEntity<>(formFieldService.deleteField(id), HttpStatus.OK);
    }

}
