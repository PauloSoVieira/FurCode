package org.mindera.fur.code.controller.formTESTE;

import org.mindera.fur.code.dto.formTESTEDTO.FormDTO;
import org.mindera.fur.code.dto.formTESTEDTO.FormFieldCreateDTO;
import org.mindera.fur.code.dto.formTESTEDTO.FormFieldDTO;
import org.mindera.fur.code.service.formTESTE.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/fields")
    public ResponseEntity<FormFieldDTO> createFormField(@RequestBody FormFieldCreateDTO createDTO) {
        return ResponseEntity.ok(formService.createFormField(createDTO));
    }

    @PostMapping("/{formId}/fields/{fieldId}")
    public ResponseEntity<FormDTO> addFieldToForm(
            @PathVariable Long formId,
            @PathVariable Long fieldId,
            @RequestBody String answer) {
        return ResponseEntity.ok(formService.addFieldToForm(formId, fieldId, answer));
    }
}