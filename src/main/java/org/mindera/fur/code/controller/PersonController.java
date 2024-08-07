package org.mindera.fur.code.controller;

import org.mindera.fur.code.dto.donation.DonationCreateDTO;
import org.mindera.fur.code.dto.donation.DonationDTO;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.dto.shelterPersonRoles.ShelterPersonRolesCreationDTO;
import org.mindera.fur.code.dto.shelterPersonRoles.ShelterPersonRolesDTO;
import org.mindera.fur.code.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/person")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(@RequestBody PersonCreationDTO personCreationDTO) {
        return new ResponseEntity<>(personService.createPerson(personCreationDTO), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/create-shelter")
    public ResponseEntity<ShelterDTO> createShelter(@PathVariable Long id, @RequestBody ShelterCreationDTO shelterCreationDTO) {
        return new ResponseEntity<>(personService.createShelter(shelterCreationDTO), HttpStatus.CREATED);
    }

    @PostMapping("/add-person-to-shelter")
    public ResponseEntity<ShelterPersonRolesDTO> addPersonToShelter(@RequestBody ShelterPersonRolesCreationDTO shelterPersonRolesCreationDTO) {
        return new ResponseEntity<>(personService.addPersonToShelter(shelterPersonRolesCreationDTO), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/donate")
    public ResponseEntity<DonationDTO> donate(@PathVariable Long id, @RequestBody DonationCreateDTO donationCreateDTO) {
        return new ResponseEntity<>(personService.donate(id, donationCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PersonDTO>> getAllPersons() {
        return new ResponseEntity<>(personService.getAllPersons(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable Long id) {
        return new ResponseEntity<>(personService.getPersonById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/get-all-donations")
    public ResponseEntity<List<DonationDTO>> getAllDonationsById(@PathVariable Long id) {
        return new ResponseEntity<>(personService.getAllDonationsById(id), HttpStatus.OK);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable Long id, @RequestBody PersonDTO personDTO) {
        return new ResponseEntity<>(personService.updatePerson(id, personDTO), HttpStatus.OK);
    }

    //Only Managers can set roles
    @PatchMapping("/set-person-role/{id}")
    public ResponseEntity<PersonDTO> setPersonRole(@PathVariable Long id, @RequestBody PersonDTO personDTO) {
        return new ResponseEntity<>(personService.setPersonRole(id, personDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
