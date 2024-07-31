package org.mindera.fur.code.controller;

import org.mindera.fur.code.dto.ShelterDTO.ShelterCreationDTO;
import org.mindera.fur.code.dto.ShelterDTO.ShelterDTO;
import org.mindera.fur.code.service.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shelter")
public class ShelterController {


    private final ShelterService shelterService;

    @Autowired
    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    //Create Shelter
    @PostMapping
    public ResponseEntity<ShelterDTO> createShelter(@RequestBody ShelterCreationDTO shelterCreationDTO) {

        ShelterCreationDTO test = shelterCreationDTO;

        return new ResponseEntity<>(shelterService.createShelter(shelterCreationDTO), HttpStatus.CREATED);
    }

    //Get all shelters
    @GetMapping("/all")
    public List<ShelterDTO> getShelters() {
        return shelterService.getAllShelters();
    }

    //Get shelter by id
    @GetMapping("/{id}")
    public ShelterDTO getShelterById(@PathVariable Long id) {
        return shelterService.getShelterById(id);
    }

    //Delete shelter by Id
    @DeleteMapping("/delete/{id}")
    public void deleteShelter(@PathVariable Long id) {
        shelterService.deleteShelter(id);
    }

    //Update shelter info
    @PatchMapping("/update/{id}")
    public ShelterDTO updateShelter(@PathVariable Long id, @RequestBody ShelterDTO shelterDTO) {
        return shelterService.updateShelter(id, shelterDTO);
    }

    /*//Request Adoption
    @GetMapping("/{id}/request/{petId}/{personId}")
    public void requestAdoption(@PathVariable Long id, @PathVariable Long petId, @PathVariable Long personId) {
        shelterService.requestAdoption(id, petId, personId);
    }

    //Add pet to shelter
    @GetMapping("/{id}/addPet")
    public void addPetToShelter(@PathVariable Long id, @PathVariable Long petId) {
        shelterService.addPetToShelter(id, petId);
    }*/
}
