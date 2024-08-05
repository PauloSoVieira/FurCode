package org.mindera.fur.code.controller;

import jakarta.validation.Valid;
import org.mindera.fur.code.dto.formsDTO.AdoptionFormCreateDTO;
import org.mindera.fur.code.dto.formsDTO.AdoptionFormDTO;
import org.mindera.fur.code.service.AdoptionFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adoptionForm")
public class AdotionFormController {

    private final AdoptionFormService adoptionFormService;

    @Autowired
    public AdotionFormController(AdoptionFormService adoptionFormService) {
        this.adoptionFormService = adoptionFormService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<AdoptionFormDTO>> adoptionFormList() {
        return new ResponseEntity<>(adoptionFormService.getAll(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AdoptionFormDTO> getAdoptionFormById(@PathVariable Long id) {
        return new ResponseEntity<>(adoptionFormService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AdoptionFormDTO> createAdoptionForm(@Valid @RequestBody AdoptionFormCreateDTO adoptionFormDto) {
        return new ResponseEntity<>(adoptionFormService.createAdoptionForm(adoptionFormDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AdoptionFormDTO> deleteAdoptionFormById(@PathVariable Long id) {
        return new ResponseEntity<>(adoptionFormService.delete(id), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdoptionFormDTO> updateAdoptionForm(@PathVariable Long id, @RequestBody AdoptionFormDTO adoptionFormDto) {
        AdoptionFormDTO dto = adoptionFormService.updateAdoptionForm(adoptionFormDto, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


}
