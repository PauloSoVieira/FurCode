package org.mindera.fur.code.controller.pet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.mindera.fur.code.dto.pet.PetBreedCreateDTO;
import org.mindera.fur.code.dto.pet.PetBreedDTO;
import org.mindera.fur.code.service.pet.PetBreedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for pet breeds.
 */
@Validated
@Tag(name = "Breed", description = "Operations for pets breeds")
@RestController
@RequestMapping("/api/v1/breed")
public class PetBreedController {
    private final PetBreedService petBreedService;

    @Autowired
    public PetBreedController(PetBreedService petBreedService) {
        this.petBreedService = petBreedService;
    }

    /**
     * Fetch a pet breed from external API if not exist in local database and save to local repository.
     *
     * @param petBreedCreateDTO the pet breed to create or fetch
     * @return the created or fetched pet breed
     */
    @Operation(summary = "Fetch a pet breed from external API if not exist in local database and save to local repository")
    @PostMapping(value = "/create-breed", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetBreedDTO> createOrFetchBreed(@RequestBody @Valid PetBreedCreateDTO petBreedCreateDTO) {
        PetBreedDTO dto = petBreedService.addOrFetchBreed(petBreedCreateDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
