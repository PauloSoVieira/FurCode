package org.mindera.fur.code.controller;

import org.mindera.fur.code.dto.donation.DonationDTO;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.service.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @CacheEvict(cacheNames = "shelters", allEntries = true)
    public ResponseEntity<ShelterDTO> createShelter(@RequestBody ShelterCreationDTO shelterCreationDTO) {
        return new ResponseEntity<>(shelterService.createShelter(shelterCreationDTO), HttpStatus.CREATED);
    }

    //Get all shelters
    @GetMapping("/all")
    @Cacheable(cacheNames = "shelters")
    public List<ShelterDTO> getAllShelters() {
        System.out.println("Cache");
        return shelterService.getAllShelters();
    }

    //Get shelter by id
    @GetMapping("/{id}")
    public ResponseEntity<ShelterDTO> getShelterById(@PathVariable Long id) {
        return new ResponseEntity<>(shelterService.getShelterById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/get-all-donations")
    public ResponseEntity<List<DonationDTO>> getAllDonationsById(@PathVariable Long id) {
        return new ResponseEntity<>(shelterService.getAllDonationsById(id), HttpStatus.OK);
    }

    //Delete shelter by Id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ShelterDTO> deleteShelter(@PathVariable Long id) {
        return new ResponseEntity<>(shelterService.deleteShelter(id), HttpStatus.NO_CONTENT);
    }

    //Update shelter info
    @PatchMapping("/update/{id}")
    @CachePut(cacheNames = "shelters", key = "#shelterDTO.id")
    public ResponseEntity<ShelterDTO> updateShelter(@PathVariable Long id, @RequestBody ShelterDTO shelterDTO) {
        return new ResponseEntity<>(shelterService.updateShelter(id, shelterDTO), HttpStatus.OK);
    }

    //Get all pets in shelter
    @GetMapping("/{id}/allPets")
    public ResponseEntity<List<PetDTO>> getAllPetsInShelter(@PathVariable Long id) {
        return new ResponseEntity<>(shelterService.getAllPetsInShelter(id), HttpStatus.OK);
    }
}
