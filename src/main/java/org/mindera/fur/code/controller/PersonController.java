package org.mindera.fur.code.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/person")
@Tag(name = "Person Controller", description = "API for managing persons")
public class PersonController {

    private final PersonService personService;

    /**
     * Constructor for PersonController.
     *
     * @param personService the service that handles person-related operations.
     */
    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Create a person.
     *
     * @param personCreationDTO The person creation DTO.
     * @return The person DTO.
     */
    @PostMapping
    @Schema(description = "Create a person")
    @CacheEvict(cacheNames = "persons", allEntries = true)
    public ResponseEntity<PersonDTO> createPerson(@RequestBody PersonCreationDTO personCreationDTO) {
        return new ResponseEntity<>(personService.createPerson(personCreationDTO), HttpStatus.CREATED);
    }

    /**
     * Create a shelter.
     *
     * @param shelterCreationDTO The shelter creation DTO.
     * @return The shelter DTO.
     */

    @PostMapping("/create-shelter")
    @Schema(description = "Create a shelter")
    public ResponseEntity<ShelterDTO> createShelter(@RequestBody ShelterCreationDTO shelterCreationDTO) {
        return new ResponseEntity<>(personService.createShelter(shelterCreationDTO), HttpStatus.CREATED);
    }

    /**
     * Add a person to a shelter.
     *
     * @param shelterPersonRolesCreationDTO The shelter person roles creation DTO.
     * @return The shelter person roles DTO.
     */

    @PostMapping("/add-person-to-shelter")
    @Schema(description = "Add a person to a shelter")
    public ResponseEntity<ShelterPersonRolesDTO> addPersonToShelter(@RequestBody ShelterPersonRolesCreationDTO shelterPersonRolesCreationDTO) {
        return new ResponseEntity<>(personService.addPersonToShelter(shelterPersonRolesCreationDTO), HttpStatus.CREATED);
    }

    /**
     * Donate to a shelter.
     *
     * @param id                The id of the person.
     * @param donationCreateDTO The donation creation DTO.
     * @return The donation DTO.
     */

    @PostMapping("/donate")
    @Schema(description = "Donate to a shelter")
    public ResponseEntity<DonationDTO> donate(@RequestBody DonationCreateDTO donationCreateDTO) throws IOException {
        return new ResponseEntity<>(personService.donate(donationCreateDTO), HttpStatus.CREATED);
    }

    /**
     * Get all persons.
     *
     * @return The list of person DTOs.
     */
    @GetMapping("/all")
    @Schema(description = "Get all persons")
    @Cacheable(cacheNames = "persons")
    public List<PersonDTO> getAllPersons() {
        return personService.getAllPersons();
    }

    /**
     * Get a person by id.
     *
     * @param id The id of the person.
     * @return The person DTO.
     */
    @GetMapping("/{id}")
    @Schema(description = "Get a person by id")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable Long id) {
        return new ResponseEntity<>(personService.getPersonById(id), HttpStatus.OK);
    }

    /**
     * Get all donations by id.
     *
     * @param id The id of the person.
     * @return The list of donation DTOs.
     */
    @GetMapping("/{id}/get-all-donations")
    @Schema(description = "Get all donations by id")
    public ResponseEntity<List<DonationDTO>> getAllDonationsById(@PathVariable Long id) {
        return new ResponseEntity<>(personService.getAllDonationsById(id), HttpStatus.OK);
    }

    /**
     * Update a person.
     *
     * @param id        The id of the person.
     * @param personDTO The person DTO.
     * @return The person DTO.
     */
    @PatchMapping("/update/{id}")
    @Schema(description = "Update a person")
    @CachePut(cacheNames = "persons", key = "#personDTO.id")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable Long id, @RequestBody PersonDTO personDTO) {
        return new ResponseEntity<>(personService.updatePerson(id, personDTO), HttpStatus.OK);
    }

    /**
     * Set the role of a person.
     *
     * @param id        The id of the person.
     * @param personDTO The person DTO.
     * @return The person DTO.
     */
    @PatchMapping("/set-person-role/{id}")
    @Schema(description = "Set the role of a person")
    public ResponseEntity<PersonDTO> setPersonRole(@PathVariable Long id, @RequestBody PersonDTO personDTO) {
        return new ResponseEntity<>(personService.setPersonRole(id, personDTO), HttpStatus.OK);
    }

    /**
     * Delete a person.
     *
     * @param id The id of the person.
     * @return No content.
     */
    @DeleteMapping("/delete/{id}")
    @Schema(description = "Delete a person")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
