package org.mindera.fur.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.mindera.fur.code.dto.pet.PetCreateDTO;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.mindera.fur.code.dto.pet.PetRecordCreateDTO;
import org.mindera.fur.code.dto.pet.PetRecordDTO;
import org.mindera.fur.code.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@Validated
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
        List<PetDTO> petDTOs = petService.findAllPets();
        return new ResponseEntity<>(petDTOs, HttpStatus.OK);
    }

    @Operation(summary = "Get a pet by id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetDTO> getPetById(@PathVariable @Valid Long id) {
        PetDTO petDTO = petService.findPetById(id);
        return new ResponseEntity<>(petDTO, HttpStatus.OK);
    }

    @Operation(summary = "Create a new pet")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetDTO> createPet(@RequestBody @Valid PetCreateDTO petCreateDTO) {
        PetDTO petDTO = petService.addPet(petCreateDTO);
        return new ResponseEntity<>(petDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a pet")
    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePet(@PathVariable @Valid Long id, @RequestBody @Valid PetCreateDTO petCreateDTO) {
        petService.updatePet(id, petCreateDTO);
    }

    @Operation(summary = "Delete a pet")
    @DeleteMapping(value = "/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePet(@PathVariable @Valid Long id) {
        petService.softDeletePet(id);
    }

    @Operation(summary = "Get all pet records by pet ID")
    @GetMapping(value = "/{id}/record", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PetRecordDTO>> getAllPetRecordsByPetId(@PathVariable @Valid Long id) {
        List<PetRecordDTO> petRecordDTO = petService.getAllPetRecordsByPetId(id);
        return new ResponseEntity<>(petRecordDTO, HttpStatus.OK);
    }

    @Operation(summary = "Create a new pet record by pet ID")
    @PostMapping(value = "/{id}/create-record", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetRecordDTO> createPetRecord(@PathVariable @Valid Long id, @RequestBody @Valid PetRecordCreateDTO petRecordCreateDTO) {
        PetRecordDTO petRecordDTO = petService.addPetRecord(id, petRecordCreateDTO);
        return new ResponseEntity<>(petRecordDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Generate a new pet description")
    @PostMapping(value = "/{id}/new-description")
    public ResponseEntity<String> generatePetDescription(@PathVariable @Valid Long id) {
        RestClient restClient = RestClient.create();
        String url = "https://llmc.ducknexus.com/api/v1/generation/send-details";
        String prompt = "Please make a simple intro text, about 100 words, to adopt this animal. I just need the text nothing more.";
        String request = String.format("%s?prompt=%s", url, prompt);
        String result = restClient.post()
                .uri(request)
                .body(petService.findPetById(id))
                .retrieve()
                .body(String.class);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}