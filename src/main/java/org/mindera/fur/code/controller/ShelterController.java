package org.mindera.fur.code.controller;

import org.mindera.fur.code.dto.ShelterDTO.ShelterDTO;
import org.mindera.fur.code.service.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ShelterController {

    @Autowired
    private ShelterService shelterService;

    //Get all shelters
    @GetMapping("/shelters")
    public List<ShelterDTO> getShelters() {
        return shelterService.getAllShelters();
    }

    //Get shelter by id
    @GetMapping("/shelters/{id}")
    public ShelterDTO getShelterById(@PathVariable Long id) {
        return shelterService.getShelterById(id);
    }

    //Request Adoption
    @GetMapping("/shelter/{id}/request/{petId}/{personId}")
    public void requestAdoption(@PathVariable Long id, @PathVariable Long petId, @PathVariable Long personId) {
        shelterService.requestAdoption(id, petId, personId);
    }

    //See all persons requests
    @GetMapping("/shelter/all/request/{personId}")
    public List<PersonDTO> getAllRequests(@PathVariable Long personId) {
        return shelterService.getAllRequests(personId);
    }

    //Add person to shelter and make define his role
    @GetMapping("/shelter/{id}/addPerson/{personId}/{role}")
    public void addPersonToShelter(@PathVariable Long id, @PathVariable Long personId, @PathVariable String role) {
        shelterService.addPersonToShelter(id, personId, role);
    }

    //Add pet to shelter
    @GetMapping("/shelter/{id}/addPet")
    public void addPetToShelter(@PathVariable Long id, @PathVariable Long petId) {
        shelterService.addPetToShelter(id, petId);
    }
}
