package org.mindera.fur.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.mindera.fur.code.dto.formsDTO.AdoptionFormCreateDTO;
import org.mindera.fur.code.dto.formsDTO.AdoptionFormDTO;
import org.mindera.fur.code.service.AdoptionFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controller class for handling adoption form related requests.
 */
@RestController
@RequestMapping("/api/v1/adoptionForm")
public class AdoptionFormController {

    private final AdoptionFormService adoptionFormService;

    @Autowired
    public AdoptionFormController(AdoptionFormService adoptionFormService) {
        this.adoptionFormService = adoptionFormService;
    }

    /**
     * Endpoint to get a list of all adoption forms.
     *
     * @return A list of AdoptionFormDTO objects.
     */
    @Operation(summary = "Get all adoption forms")
    @GetMapping("/all")
    public ResponseEntity<List<AdoptionFormDTO>> adoptionFormList() {
        return new ResponseEntity<>(adoptionFormService.getAll(), HttpStatus.OK);
    }

    /**
     * Endpoint to get an adoption form by its ID.
     *
     * @param id The ID of the adoption form.
     * @return An AdoptionFormDTO object.
     */
    @Operation(summary = "Get adoption form by ID")
    @GetMapping("/{id}")
    public ResponseEntity<AdoptionFormDTO> getAdoptionFormById(@PathVariable Long id) {
        return new ResponseEntity<>(adoptionFormService.getById(id), HttpStatus.OK);
    }

    /**
     * Endpoint to create a new adoption form.
     *
     * @param adoptionFormDto The AdoptionFormCreateDTO object.
     * @return The created AdoptionFormDTO object.
     */

    @PostMapping
    public ResponseEntity<AdoptionFormDTO> createAdoptionForm(@Valid @RequestBody AdoptionFormCreateDTO adoptionFormDto) {
        return new ResponseEntity<>(adoptionFormService.createAdoptionForm(adoptionFormDto), HttpStatus.CREATED);
    }

    /**
     * Endpoint to delete an adoption form by its ID.
     *
     * @param id The ID of the adoption form to delete.
     */

    @Operation(summary = "Delete adoption form by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<AdoptionFormDTO> deleteAdoptionFormById(@PathVariable Long id) {
        return new ResponseEntity<>(adoptionFormService.delete(id), HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint to update an adoption form by its ID.
     *
     * @param id              The ID of the adoption form.
     * @param adoptionFormDto The AdoptionFormDTO object.
     * @return The updated AdoptionFormDTO object.
     */
    @Operation(summary = "Update an adoption form by ID")
    @PutMapping("/{id}")
    public ResponseEntity<AdoptionFormDTO> updateAdoptionForm(@PathVariable Long id, @RequestBody AdoptionFormDTO adoptionFormDto) {
        AdoptionFormDTO dto = adoptionFormService.updateAdoptionForm(adoptionFormDto, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


}
