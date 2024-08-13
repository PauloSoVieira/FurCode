package org.mindera.fur.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.mindera.fur.code.dto.pet.*;
import org.mindera.fur.code.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Cacheable(cacheNames = "pets")
    public List<PetDTO> getAllPets() {
        List<PetDTO> petDTOs = petService.findAllPets();
        return petDTOs;
    }

    @Operation(summary = "Get a pet by id")

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetDTO> getPetById(@PathVariable @Valid Long id) {
        PetDTO petDTO = petService.findPetById(id);
        return new ResponseEntity<>(petDTO, HttpStatus.OK);
    }

    @Operation(summary = "Create a new pet")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(cacheNames = "pets", allEntries = true)
    public ResponseEntity<PetDTO> createPet(@RequestBody @Valid PetCreateDTO petCreateDTO) {
        PetDTO petDTO = petService.addPet(petCreateDTO);
        return new ResponseEntity<>(petDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a pet")
    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @CachePut(cacheNames = "pets", key = "#petDTO.id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePet(@PathVariable @Valid Long id, @RequestBody @Valid PetUpdateDTO petUpdateDTO) {
        petService.updatePet(id, petUpdateDTO);
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

    @Operation(summary = "Create or fetch a pet breed from external DOG API")
    @PostMapping(value = "/breed", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetBreedDTO> createOrFetchBreed(@RequestBody @Valid PetBreedCreateDTO petBreedCreateDTO) {
        PetBreedDTO breedDTO = petService.addOrFetchBreed(petBreedCreateDTO);
        return new ResponseEntity<>(breedDTO, HttpStatus.CREATED);
    }
}
