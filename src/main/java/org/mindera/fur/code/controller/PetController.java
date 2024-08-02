package org.mindera.fur.code.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.mindera.fur.code.dto.pet.PetCreateDTO;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.mindera.fur.code.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(path = "/api/v1/pet")
public class PetController {

    PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetDTO> createPet(@Valid @RequestBody PetCreateDTO petCreateDTO) {
        try {
            PetDTO petDTO = petService.createPet(petCreateDTO);
            return new ResponseEntity<>(petDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /*
    @GetMapping(value = "/{id}/medicalRecord", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicalRecordDTO> getMedicalRecord(@Valid @PathVariable("id") Long id) {
        try {
            MedicalRecordDTO medicalRecordDTO = petService.getMedicalRecord(id);
            return new ResponseEntity<>(medicalRecordDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
     */

}
