package org.mindera.fur.code.controller;

import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailCreationDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailDTO;
import org.mindera.fur.code.service.AdoptionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adoption-request")
public class AdoptionRequestController {

    @Autowired
    AdoptionRequestService adoptionRequestService;

    //Create an adoption request
    @PostMapping
    public ResponseEntity<AdoptionRequestDTO> createAdoptionRequest(@RequestBody AdoptionRequestCreationDTO adoptionRequestCreationDTO) {
        return new ResponseEntity<>(adoptionRequestService.createAdoptionRequest(adoptionRequestCreationDTO), HttpStatus.CREATED);
    }

    //Change some info from the request
    @PatchMapping("/update/{id}")
    public ResponseEntity<AdoptionRequestDTO> updateAdoptionRequest(@PathVariable Long id, @RequestBody AdoptionRequestDTO adoptionRequestDTO) {
        return new ResponseEntity<>(adoptionRequestService.updateAdoptionRequest(id, adoptionRequestDTO), HttpStatus.OK);
    }

    //Get all requests
    @GetMapping("/all")
    public ResponseEntity<List<AdoptionRequestDTO>> getAllAdoptionRequests() {
        return new ResponseEntity<>(adoptionRequestService.getAllAdoptionRequests(), HttpStatus.OK);
    }

    //Get request by id
    @GetMapping("/{id}")
    public ResponseEntity<AdoptionRequestDTO> getAdoptionRequestById(@PathVariable Long id) {
        return new ResponseEntity<>(adoptionRequestService.getAdoptionRequestById(id), HttpStatus.OK);
    }

    //Delete request
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AdoptionRequestDTO> deleteAdoptionRequestById(@PathVariable Long id) {
        adoptionRequestService.deleteAdoptionRequestById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Get all details of the adoption request
    @GetMapping("/{id}/details")
    public ResponseEntity<List<RequestDetailDTO>> getAllRequestDetails(@PathVariable Long id) {
        return new ResponseEntity<>(adoptionRequestService.getAllRequestDetails(id), HttpStatus.OK);
    }

    //Create a new request detail
    @PostMapping("/{id}/detail-request")
    public ResponseEntity<RequestDetailDTO> createRequestDetail(@PathVariable Long id, @RequestBody RequestDetailCreationDTO requestDetailCreationDTO) {
        return new ResponseEntity<>(adoptionRequestService.createRequestDetail(id, requestDetailCreationDTO), HttpStatus.CREATED);
    }

    //Get request detail by id
    @GetMapping("/{id}/detail-request/{detailId}")
    public ResponseEntity<RequestDetailDTO> getRequestDetailById(@PathVariable Long id, @PathVariable Long detailId) {
        return new ResponseEntity<>(adoptionRequestService.getRequestDetailById(id, detailId), HttpStatus.OK);
    }
}
