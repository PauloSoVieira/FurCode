package org.mindera.fur.code.controller.formTESTE;

import io.swagger.v3.oas.annotations.Operation;
import org.mindera.fur.code.dto.formTESTEDTO.FieldAnswerDTO;
import org.mindera.fur.code.dto.formTESTEDTO.FormDTO;
import org.mindera.fur.code.dto.formTESTEDTO.FormFieldCreateDTO;
import org.mindera.fur.code.dto.formTESTEDTO.FormFieldDTO;
import org.mindera.fur.code.service.formTESTE.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<FormDTO> getForm(@PathVariable Long id) {
        return ResponseEntity.ok(formService.getForm(id));
    }

    @PostMapping("/{formId}/fields/{fieldId}")
    @Operation(summary = "Add a field to a form with an answer")
    public ResponseEntity<FormDTO> addFieldToForm(
            @PathVariable Long formId,
            @PathVariable Long fieldId,
            @RequestBody FieldAnswerDTO answerDTO) {
        return ResponseEntity.ok(formService.addFieldToForm(formId, fieldId, answerDTO.getAnswer()));
    }

    @GetMapping("/{formId}/fields")
    @Operation(summary = "Get all fields for a form")
    public ResponseEntity<List<FormFieldDTO>> getFieldsForForm(@PathVariable Long formId) {
        return ResponseEntity.ok(formService.getFieldsForForm(formId));
    }
}