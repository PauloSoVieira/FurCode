package org.mindera.fur.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.mindera.fur.code.dto.donation.DonationDTO;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.service.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling shelter related requests.
 */
@RestController
@RequestMapping("/api/v1/shelter")
@Tag(name = "Shelter Controller", description = "API for managing shelters")
public class ShelterController {


    private final ShelterService shelterService;

    /**
     * Constructor for the ShelterController
     *
     * @param shelterService
     */
    @Autowired
    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    //Create Shelter

    /**
     * Endpoint to create a shelter.
     *
     * @param shelterCreationDTO The ShelterCreationDTO object.
     * @return The created ShelterDTO object.
     */
    @PostMapping
    @Schema(description = "Create a shelter")
    @Operation(summary = "Create a shelter", description = "Creates a new shelter with the provided data")
    @CacheEvict(cacheNames = "shelters", allEntries = true)

    public ResponseEntity<ShelterDTO> createShelter(@RequestBody ShelterCreationDTO shelterCreationDTO) {
        return new ResponseEntity<>(shelterService.createShelter(shelterCreationDTO), HttpStatus.CREATED);
    }


    /**
     * Endpoint to get all shelters.
     *
     * @return The list of all shelters.
     */
    @GetMapping("/all")
    @Schema(description = "Get all shelters")
    @Operation(summary = "Get all shelters", description = "Returns a list of all shelters")
    @Cacheable(cacheNames = "shelters")
    public List<ShelterDTO> getAllShelters() {
        System.out.println("Cache");
        return shelterService.getAllShelters();
    }

    //Get shelter by id

    /**
     * Endpoint to get a shelter by id.
     *
     * @param id The id of the shelter.
     * @return The ShelterDTO object.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a shelter by id", description = "Returns a shelter with the specified id")
    @Schema(description = "Get a shelter by id")
    public ResponseEntity<ShelterDTO> getShelterById(@PathVariable Long id) {
        return new ResponseEntity<>(shelterService.getShelterById(id), HttpStatus.OK);
    }

    //Get all donations in shelter

    /**
     * Endpoint to get all donations in a shelter.
     *
     * @param id The id of the shelter.
     * @return The list of donations.
     */
    @GetMapping("/{id}/get-all-donations")
    @Operation(summary = "Get all donations in a shelter", description = "Returns a list of donations in a shelter")
    @Schema(description = "Get all donations in a shelter")
    public ResponseEntity<List<DonationDTO>> getAllDonationsById(@PathVariable Long id) {
        return new ResponseEntity<>(shelterService.getAllDonationsById(id), HttpStatus.OK);
    }

    //Delete shelter by Id

    /**
     * Endpoint to delete a shelter by id.
     *
     * @param id The id of the shelter.
     * @return The deleted shelter.
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a shelter by id", description = "Deletes a shelter with the specified id")
    @Schema(description = "Delete a shelter by id")
    public ResponseEntity<ShelterDTO> deleteShelter(@PathVariable Long id) {
        return new ResponseEntity<>(shelterService.deleteShelter(id), HttpStatus.NO_CONTENT);
    }

    //Update shelter info

    /**
     * Endpoint to update a shelter.
     *
     * @param id         The id of the shelter.
     * @param shelterDTO The ShelterDTO object.
     * @return The updated ShelterDTO object.
     */
    @PatchMapping("/update/{id}")
    @Schema(description = "Update a shelter")
    @Operation(summary = "Update a shelter", description = "Updates a shelter with the provided data")
    @CachePut(cacheNames = "shelters", key = "#shelterDTO.id")
    public ResponseEntity<ShelterDTO> updateShelter(@PathVariable Long id, @RequestBody ShelterDTO shelterDTO) {
        return new ResponseEntity<>(shelterService.updateShelter(id, shelterDTO), HttpStatus.OK);
    }

    //Get all pets in shelter

    /**
     * Endpoint to get all pets in a shelter.
     *
     * @param id The id of the shelter.
     * @return The list of pets.
     */
    @GetMapping("/{id}/allPets")
    @Operation(summary = "Get all pets in a shelter", description = "Returns a list of pets in a shelter")
    @Schema(description = "Get all pets in a shelter")
    public ResponseEntity<List<PetDTO>> getAllPetsInShelter(@PathVariable Long id) {
        return new ResponseEntity<>(shelterService.getAllPetsInShelter(id), HttpStatus.OK);
    }
}
