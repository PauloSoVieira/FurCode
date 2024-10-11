package org.mindera.fur.code.controller.pet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.mindera.fur.code.dto.pet.*;
import org.mindera.fur.code.service.AIService;
import org.mindera.fur.code.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Pet Controller for managing Pet entities.
 */
@Validated
@Tag(name = "Pet Controller", description = "Operations for pets")
@RestController
@RequestMapping(path = "/api/v1/pet")
public class PetController {
    private final PetService petService;
    private final AIService aiService;

    @Autowired
    public PetController(PetService petService, AIService aiService) {
        this.petService = petService;
        this.aiService = aiService;
    }

    /**
     * Get all pets.
     *
     * @return a list of all pets
     */
    @Operation(summary = "Get all active pets")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PetDTO>> getAllPets() {
        List<PetDTO> petDTOs = petService.findAllPets();
        return new ResponseEntity<>(petDTOs, HttpStatus.OK);
    }

    /**
     * Get a pet by ID.
     *
     * @param id The ID of the pet.
     * @return The pet.
     */
    @Operation(summary = "Get a active pet by ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetDTO> getPetById(@PathVariable @NotNull @Positive Long id) {
        PetDTO petDTO = petService.findPetById(id);
        return new ResponseEntity<>(petDTO, HttpStatus.OK);
    }

    /**
     * Create a new pet.
     *
     * @param petCreateDTO The pet to create.
     * @return The pet.
     */
    @Operation(summary = "Create a new pet")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetDTO> createPet(@RequestBody @Valid PetCreateDTO petCreateDTO) {
        PetDTO petDTO = petService.addPet(petCreateDTO);
        return new ResponseEntity<>(petDTO, HttpStatus.CREATED);
    }

    /**
     * Update a pet.
     *
     * @param id           The ID of the pet.
     * @param petUpdateDTO The pet to update.
     */
    @Operation(summary = "Update a pet")
    @PatchMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetDTO> updatePet(@PathVariable @NotNull @Positive Long id, @RequestBody @Valid PetUpdateDTO petUpdateDTO) {
        PetDTO petDTO = petService.updatePet(id, petUpdateDTO);
        return new ResponseEntity<>(petDTO, HttpStatus.OK);
    }

    /**
     * Delete a pet.
     *
     * @param id The ID of the pet.
     */
    @Operation(summary = "Delete a pet")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable @NotNull @Positive Long id) {
        petService.softDeletePet(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Get all active pet records by pet ID.
     *
     * @param id The ID of the active pet.
     * @return A list of all active pet records.
     */
    @Operation(summary = "Get all active pet records by pet ID")
    @GetMapping(value = "/{id}/record", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PetRecordDTO>> getAllPetRecordsByPetId(@PathVariable @NotNull @Positive Long id) {
        List<PetRecordDTO> petRecordDTO = petService.getAllPetRecordsByPetId(id);
        return new ResponseEntity<>(petRecordDTO, HttpStatus.OK);
    }

    /**
     * Create a new pet record by pet ID.
     *
     * @param id                 The ID of the pet.
     * @param petRecordCreateDTO The pet record to create.
     * @return The created pet record.
     */
    @Operation(summary = "Create a new pet record by pet ID")
    @PostMapping(value = "/{id}/create-record", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetRecordDTO> createPetRecord(@PathVariable @NotNull @Positive Long id, @RequestBody @Valid PetRecordCreateDTO petRecordCreateDTO) {
        PetRecordDTO petRecordDTO = petService.addPetRecord(id, petRecordCreateDTO);
        return new ResponseEntity<>(petRecordDTO, HttpStatus.CREATED);
    }

    /**
     * Retrieves all soft-deleted pets.
     *
     * @return List of soft-deleted pet DTOs.
     */
    @Operation(summary = "Get all deleted pets")
    @GetMapping("/deleted")
    public ResponseEntity<List<PetDTO>> getAllDeletedPets() {
        List<PetDTO> deletedPets = petService.findAllDeletedPets();
        return new ResponseEntity<>(deletedPets, HttpStatus.OK);
    }

    /**
     * Retrieves a soft-deleted pet by ID.
     *
     * @param id The ID of the pet.
     * @return The soft-deleted pet DTO.
     */
    @Operation(summary = "Get a deleted pet by ID")
    @GetMapping("/deleted/{id}")
    public ResponseEntity<PetDTO> getDeletedPetById(@PathVariable @NotNull @Positive Long id) {
        PetDTO petDTO = petService.findDeletedPetById(id);
        return new ResponseEntity<>(petDTO, HttpStatus.OK);
    }

    /**
     * Restores a soft-deleted pet.
     *
     * @param id The ID of the pet to restore.
     */
    @Operation(summary = "Restore a deleted pet")
    @PostMapping("/restore/{id}")
    public ResponseEntity<Void> restorePet(@PathVariable @NotNull @Positive Long id) {
        petService.restorePet(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves all deleted pet records for a specific pet ID.
     *
     * @param petId The ID of the pet.
     * @return List of soft-deleted PetRecordDTOs.
     */
    @Operation(summary = "Retrieves all deleted pet records for a specific pet ID")
    @GetMapping("/{petId}/deleted-record")
    public ResponseEntity<List<PetRecordDTO>> getDeletedPetRecordsByPetId(@PathVariable Long petId) {
        List<PetRecordDTO> deletedPetRecords = petService.findDeletedPetRecordEntityByPetId(petId);
        return new ResponseEntity<>(deletedPetRecords, HttpStatus.OK);
    }

    /**
     * Generate a new pet description with AI.
     *
     * @param id The ID of the pet.
     * @return The generated pet description.
     */
    @Operation(summary = "Generate a new pet description with AI")
    @PostMapping(value = "/{id}/new-description")
    public ResponseEntity<String> generatePetDescription(@PathVariable @NotNull @Positive Long id) {
        return new ResponseEntity<>(aiService.generateNewPetDescription(petService.findPetById(id)), HttpStatus.OK);
    }

    /**
     * Search a pet with natural language.
     *
     * @param searchQuery The search query.
     * @return The search result.
     */
    @Operation(summary = "Search a pet with natural language")
    @PostMapping(value = "/search/nl/{searchQuery}")
    public ResponseEntity<String> searchPetWithNaturalLanguage(@PathVariable @NotEmpty String searchQuery) {
        return new ResponseEntity<>(aiService.generateNewPetSearchQuery(searchQuery), HttpStatus.OK);
    }
}
