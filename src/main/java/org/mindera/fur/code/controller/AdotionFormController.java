package org.mindera.fur.code.controller;

import org.mindera.fur.code.dto.AdoptionFormCreateDTO;
import org.mindera.fur.code.dto.AdoptionFormDTO;
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

    @PostMapping("/{id}")
    public ResponseEntity<AdoptionFormDTO> addAdoptionForm(@PathVariable Long id, @RequestBody AdoptionFormCreateDTO adoptionFormDto) {
        return new ResponseEntity<>(adoptionFormService.addAdoptionForm(adoptionFormDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AdoptionFormDTO> deleteAdoptionFormById(@PathVariable Long id) {
        return new ResponseEntity<>(adoptionFormService.delete(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdoptionFormDTO> updateAdoptionForm(@PathVariable Long id, @RequestBody AdoptionFormDTO adoptionFormDto) {
        AdoptionFormDTO dto = adoptionFormService.updateAdoptionForm(adoptionFormDto, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}
