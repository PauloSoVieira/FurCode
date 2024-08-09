//package org.mindera.fur.code.controller.form;
//
//import io.swagger.v3.oas.annotations.Operation;
//import org.mindera.fur.code.dto.form.FormFieldCreateDTO;
//import org.mindera.fur.code.dto.form.FormFieldDTO;
//import org.mindera.fur.code.service.form.FormFieldService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/formfields")
//public class FormFieldController {
//    private final FormFieldService formFieldService;
//
//    @Autowired
//    public FormFieldController(FormFieldService formFieldService) {
//        this.formFieldService = formFieldService;
//    }
//
//    @PostMapping
//    @Operation(summary = "Create a new form field")
//    public ResponseEntity<FormFieldDTO> createFormField(@RequestBody FormFieldCreateDTO createDTO) {
//        return ResponseEntity.ok(formFieldService.createFormField(createDTO));
//    }
//
//    // Other methods...
//}