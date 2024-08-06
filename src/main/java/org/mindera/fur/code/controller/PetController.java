package org.mindera.fur.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.mindera.fur.code.dto.pet.PetCreateDTO;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.mindera.fur.code.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

//@Validated
@Tag(name = "Pet", description = "Operations for pets")
@RestController
@RequestMapping(path = "/api/v1/pet")
public class PetController {
    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @Operation(summary = "Get all pets")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PetDTO>> getAllPets() {
        try {
            List<PetDTO> petDTOs = petService.findAllPets();
            return new ResponseEntity<>(petDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Get a pet by id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetDTO> getPetById(@Valid @PathVariable Long id) {
        try {
            PetDTO petDTO = petService.findPetById(id);
            return new ResponseEntity<>(petDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a new pet")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetDTO> createPet(@Valid @RequestBody PetCreateDTO petCreateDTO) {
        try {
            PetDTO petDTO = petService.addPet(petCreateDTO);
            // TODO: maybe add findPetById(id) to the response or on server side
            return new ResponseEntity<>(petDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update a pet")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetDTO> updatePet(@Valid @PathVariable Long id, @Valid @RequestBody PetCreateDTO petCreateDTO) {
        try {
            PetDTO petDTO = petService.updatePet(id, petCreateDTO);
            // TODO: maybe add findPetById(id) to the response or on server side
            return new ResponseEntity<>(petDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Delete a pet")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deletePet(@Valid @PathVariable Long id) {
        try {
            petService.softDeletePet(id);
            // TODO: maybe add findPetById(id) to the response or on server side but not sure if it's needed because is void not a GET
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

    @GetMapping(value = "/{id}/new-description", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPetDescription(@Valid @PathVariable("id") Long id) {

        RestClient restClient = RestClient.create();
        String url = "http://localhost:8090/api/v1/generation/send-details?prompt=Please make a simple intro text, about 100 words, to adopt this animal. I just need the text nothing more.";
        String result = restClient.post()
                .uri(url)
                .body(petService.findPetById(id))
                .retrieve()
                .body(String.class);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
