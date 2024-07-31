package org.mindera.fur.code.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.mindera.fur.code.dto.MedicalRecordDTO;
import org.mindera.fur.code.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(path = "/api/v1/pet")
@Tag(name = "Pets")
public class PetController {

    PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping(value = "/{id}/medicalRecord", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicalRecordDTO> getMedicalRecord(@PathVariable("id") Long id) {
        try {
            MedicalRecordDTO medicalRecordDTO = petService.getMedicalRecord(id);
            return new ResponseEntity<>(medicalRecordDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
