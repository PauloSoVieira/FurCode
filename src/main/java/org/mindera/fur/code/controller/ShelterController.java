package org.mindera.fur.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.mindera.fur.code.dto.donation.DonationDTO;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.dto.shelter.ShelterUpdateDTO;
import org.mindera.fur.code.service.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Shelter Controller class for handling shelter related requests.
 */
@Validated
@RestController
@RequestMapping("/api/v1/shelter")
@Tag(name = "Shelter Controller", description = "Operations for shelters")
public class ShelterController {

    private final ShelterService shelterService;

    /**
     * Constructor for the ShelterController
     *
     * @param shelterService the service that handles shelter-related operations.
     */
    @Autowired
    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    /**
     * Endpoint to get all shelters.
     *
     * @return The list of all shelters.
     */
    @Operation(summary = "Get all shelters", description = "Returns a list of all shelters")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ShelterDTO>> getAllShelters() {
        List<ShelterDTO> shelterDTOs = shelterService.getAllShelters();
        return new ResponseEntity<>(shelterDTOs, HttpStatus.OK);
    }

    /**
     * Endpoint to get a shelter by ID.
     *
     * @param id The ID of the shelter.
     * @return The ShelterDTO object.
     */
    @Operation(summary = "Get a shelter by id", description = "Returns a shelter with the specified id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShelterDTO> getShelterById(@PathVariable @NotNull @Positive Long id) {
        return new ResponseEntity<>(shelterService.getShelterById(id), HttpStatus.OK);
    }

    /**
     * Endpoint to create a shelter.
     *
     * @param shelterCreationDTO The ShelterCreationDTO object.
     * @return The created ShelterDTO object.
     */
    @Operation(summary = "Create a shelter", description = "Creates a new shelter with the provided data")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShelterDTO> createShelter(@RequestBody @Valid ShelterCreationDTO shelterCreationDTO) {
        return new ResponseEntity<>(shelterService.createShelter(shelterCreationDTO), HttpStatus.CREATED);
    }

    /**
     * Endpoint to update a shelter.
     *
     * @param id         The id of the shelter.
     * @param shelterUpdateDTO The ShelterUpdateDTO object.
     * @return The updated ShelterDTO object.
     */
    @Operation(summary = "Update a shelter", description = "Updates a shelter with the provided data")
    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShelterDTO> updateShelter(@PathVariable @NotNull @Positive Long id, @RequestBody @Valid ShelterUpdateDTO shelterUpdateDTO) {
        return new ResponseEntity<>(shelterService.updateShelter(id, shelterUpdateDTO), HttpStatus.OK);
    }

    /**
     * Endpoint to delete a shelter by id.
     *
     * @param id The id of the shelter.
     * @return The deleted shelter.
     */
    @Operation(summary = "Delete a shelter by id", description = "Deletes a shelter with the specified id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteShelter(@PathVariable @NotNull @Positive Long id) {
        shelterService.deleteShelter(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint to get all donations in a shelter.
     *
     * @param id The id of the shelter.
     * @return The list of donations.
     */
    @Operation(summary = "Get all donations in a shelter", description = "Returns a list of donations in a shelter")
    @GetMapping("/{id}/get-all-donations")
    public ResponseEntity<List<DonationDTO>> getAllDonationsById(@PathVariable @NotNull @Positive Long id) {
        return new ResponseEntity<>(shelterService.getAllDonationsById(id), HttpStatus.OK);
    }

    /**
     * Endpoint to get all pets in a shelter.
     *
     * @param id The id of the shelter.
     * @return The list of pets.
     */
    @Operation(summary = "Get all pets in a shelter", description = "Returns a list of pets in a shelter")
    @GetMapping(value = "/{id}/allPets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PetDTO>> getAllPetsInShelter(@PathVariable @NotNull @Positive Long id) {
        return new ResponseEntity<>(shelterService.getAllPetsInShelter(id), HttpStatus.OK);
    }
}
