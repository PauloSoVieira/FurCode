package org.mindera.fur.code.controller.person_preferences;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.mindera.fur.code.dto.person_preferences.FavoriteDTO;
import org.mindera.fur.code.service.person_preferences.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Favorite Controller", description = "Operations for favorites")
@Validated
@RestController
@RequestMapping(path = "/api/v1/favorite")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @Operation(summary = "Add a favorite")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FavoriteDTO> addFavorite(@RequestBody @Valid FavoriteDTO favoriteDTO) {
        FavoriteDTO favoriteDto = favoriteService.addFavorite(favoriteDTO.getPersonId(), favoriteDTO.getPetId());
        return new ResponseEntity<>(favoriteDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all favorites for a person")
    @GetMapping(value = "/person/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FavoriteDTO>> getFavoritesByPerson(@PathVariable @NotNull @Positive Long id) {
        List<FavoriteDTO> favorites = favoriteService.getFavoritesByPerson(id);
        return new ResponseEntity<>(favorites, HttpStatus.OK);
    }

    @Operation(summary = "Get all favorites for a pet")
    @GetMapping(value = "/pet/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FavoriteDTO>> getFavoritesByPet(@PathVariable @NotNull @Positive Long id) {
        List<FavoriteDTO> favorites = favoriteService.getFavoritesByPet(id);
        return new ResponseEntity<>(favorites, HttpStatus.OK);
    }

    @Operation(summary = "Check if a pet is favorited by a person")
    @GetMapping(value = "/{personId}/{petId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> isFavorite(
            @PathVariable @NotNull @Positive Long personId,
            @PathVariable @NotNull @Positive Long petId) {
        boolean isFavorite = favoriteService.isFavorite(personId, petId);
        return new ResponseEntity<>(isFavorite, HttpStatus.OK);
    }

    @Operation(summary = "Remove a favorite")
    @DeleteMapping(value = "/delete/{personId}/{petId}")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable @NotNull @Positive Long personId,
            @PathVariable @NotNull @Positive Long petId
    ) {
        favoriteService.removeFavorite(personId, petId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
