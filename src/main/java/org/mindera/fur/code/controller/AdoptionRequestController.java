package org.mindera.fur.code.controller;

import io.swagger.v3.oas.annotations.Operation;
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

/**
 * Controller class for handling adoption request related requests.
 */
@RestController
@RequestMapping("/api/v1/adoption-request")
public class AdoptionRequestController {

    private final AdoptionRequestService adoptionRequestService;

    /**
     * Constructor for the AdoptionRequestController
     *
     * @param adoptionRequestService
     */
    @Autowired
    public AdoptionRequestController(AdoptionRequestService adoptionRequestService) {
        this.adoptionRequestService = adoptionRequestService;
    }

    //Create an adoption request

    /**
     * Endpoint to create an adoption request.
     *
     * @param adoptionRequestCreationDTO The AdoptionRequestCreationDTO object.
     * @return The created AdoptionRequestDTO object.
     */
    @Operation(summary = "Create an adoption request")
    @PostMapping
    public ResponseEntity<AdoptionRequestDTO> createAdoptionRequest(@RequestBody AdoptionRequestCreationDTO adoptionRequestCreationDTO) {
        return new ResponseEntity<>(adoptionRequestService.createAdoptionRequest(adoptionRequestCreationDTO), HttpStatus.CREATED);
    }

    //Change some info from the request

    /**
     * Endpoint to update an adoption request.
     *
     * @param id                 The id of the adoption request.
     * @param adoptionRequestDTO The AdoptionRequestDTO object.
     * @return The updated AdoptionRequestDTO object.
     */
    @Operation(summary = "Update an adoption request")
    @PatchMapping("/update/{id}")
    public ResponseEntity<AdoptionRequestDTO> updateAdoptionRequest(@PathVariable Long id, @RequestBody AdoptionRequestDTO adoptionRequestDTO) {
        return new ResponseEntity<>(adoptionRequestService.updateAdoptionRequest(id, adoptionRequestDTO), HttpStatus.OK);
    }

    //Get all requests

    /**
     * Endpoint to get all adoption requests.
     *
     * @return The list of all adoption requests.
     */
    @Operation(summary = "Get all adoption requests")
    @GetMapping("/all")
    public ResponseEntity<List<AdoptionRequestDTO>> getAllAdoptionRequests() {
        return new ResponseEntity<>(adoptionRequestService.getAllAdoptionRequests(), HttpStatus.OK);
    }

    //Get request by id

    /**
     * Endpoint to get an adoption request by id.
     *
     * @param id The id of the adoption request.
     * @return The AdoptionRequestDTO object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdoptionRequestDTO> getAdoptionRequestById(@PathVariable Long id) {
        return new ResponseEntity<>(adoptionRequestService.getAdoptionRequestById(id), HttpStatus.OK);
    }

    //Delete request

    /**
     * Endpoint to delete an adoption request by id.
     *
     * @param id The id of the adoption request.
     * @return The deleted adoption request.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AdoptionRequestDTO> deleteAdoptionRequestById(@PathVariable Long id) {
        adoptionRequestService.deleteAdoptionRequestById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    //Get all details of the adoption request

    /**
     * Endpoint to get all request details of an adoption request.
     *
     * @param id The id of the adoption request.
     * @return The list of request details.
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<List<RequestDetailDTO>> getAllRequestDetails(@PathVariable Long id) {
        return new ResponseEntity<>(adoptionRequestService.getAllRequestDetails(id), HttpStatus.OK);
    }


    //Create a new request detail

    /**
     * Endpoint to create a new request detail.
     *
     * @param id                       The id of the adoption request.
     * @param requestDetailCreationDTO The RequestDetailCreationDTO object.
     * @return The created RequestDetailDTO object.
     */
    @PostMapping("/{id}/detail-request")
    public ResponseEntity<RequestDetailDTO> createRequestDetail(@PathVariable Long id, @RequestBody RequestDetailCreationDTO requestDetailCreationDTO) {
        return new ResponseEntity<>(adoptionRequestService.createRequestDetail(id, requestDetailCreationDTO), HttpStatus.CREATED);
    }

    //Get request detail by id

    /**
     * Endpoint to get a request detail by id.
     *
     * @param id       The id of the adoption request.
     * @param detailId The id of the request detail.
     * @return The RequestDetailDTO object.
     */
    @GetMapping("/{id}/detail-request/{detailId}")
    public ResponseEntity<RequestDetailDTO> getRequestDetailById(@PathVariable Long id, @PathVariable Long detailId) {
        return new ResponseEntity<>(adoptionRequestService.getRequestDetailById(id, detailId), HttpStatus.OK);
    }
}
