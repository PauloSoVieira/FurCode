package org.mindera.fur.code.controller.external_apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.mindera.fur.code.dto.external_apis.dog_api.DogBreedDTO;
import org.mindera.fur.code.dto.external_apis.dog_api.DogBreedsNamesDTO;
import org.mindera.fur.code.service.external_apis.DogApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the external dog API.
 */
@Validated
@Tag(name = "External Dog API", description = "Operations for the external Dog API")
@RestController
@RequestMapping("/api/dogs")
public class DogApiController {
    private final DogApiService dogApiService;

    @Autowired
    public DogApiController(DogApiService dogApiService) {
        this.dogApiService = dogApiService;
    }

    /**
     * Get a dog breed and description by ID
     *
     * @param id the ID of the dog breed
     * @return the dog breed and description
     */
    @Operation(summary = "Get a dog breed and description by ID")
    @GetMapping(value = "/breed-and-description/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DogBreedDTO> getBreedAndDescriptionById(@PathVariable @Valid String id) {
        DogBreedDTO dto = dogApiService.fetchBreedById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * Get all dog breeds names - from all pages
     *
     * @return the list of dog breed names
     */
    @Operation(summary = "Get all dog breeds names - from all pages")
    @GetMapping(value = "/all-breeds-names", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DogBreedsNamesDTO> getAllBreedsNames() {
        DogBreedsNamesDTO dto = dogApiService.fetchAllBreedsNames();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * Get a dog breed info by name - search from all pages
     *
     * @param name the name of the dog breed
     * @return the dog breed info
     */
    @Operation(summary = "Get a dog breed info by name - search from all pages")
    @GetMapping("/breed/{name}")
    public ResponseEntity<DogBreedDTO> getBreedInfoByName(@PathVariable @Valid String name) {
        DogBreedDTO dto = dogApiService.getBreedByName(name);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
