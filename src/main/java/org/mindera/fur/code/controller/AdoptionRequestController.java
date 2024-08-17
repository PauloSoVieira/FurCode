package org.mindera.fur.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailCreationDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailDTO;
import org.mindera.fur.code.service.AdoptionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling adoption request related requests.
 */
@RestController
@RequestMapping("/api/v1/adoption-request")
@Tag(name = "Adoption Request Controller", description = "Endpoint for handling adoption requests")
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


    /**
     * Endpoint to create an adoption request.
     *
     * @param adoptionRequestCreationDTO The AdoptionRequestCreationDTO object.
     * @return The created AdoptionRequestDTO object.
     */
    @PostMapping(consumes = {"application/json", "application/json;charset=UTF-8"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create an adoption request", description = "Creates a new adoption request with the provided data")
    @Schema(description = "Create an adoption request")
    public ResponseEntity<AdoptionRequestDTO> createAdoptionRequest(@Valid @RequestBody AdoptionRequestCreationDTO adoptionRequestCreationDTO) {
        return new ResponseEntity<>(adoptionRequestService.createAdoptionRequest(adoptionRequestCreationDTO), HttpStatus.CREATED);
    }


    /**
     * Endpoint to update an adoption request.
     *
     * @param id                 The id of the adoption request.
     * @param adoptionRequestDTO The AdoptionRequestDTO object.
     * @return The updated AdoptionRequestDTO object.
     */
    @PatchMapping("/update/{id}")
    @Operation(summary = "Update an adoption request", description = "Updates an adoption request with the provided data")
    @Schema(description = "Update an adoption request")
    public ResponseEntity<AdoptionRequestDTO> updateAdoptionRequest(@PathVariable Long id, @RequestBody AdoptionRequestDTO adoptionRequestDTO) {
        return new ResponseEntity<>(adoptionRequestService.updateAdoptionRequest(id, adoptionRequestDTO), HttpStatus.OK);
    }


    /**
     * Endpoint to get all adoption requests.
     *
     * @return The list of all adoption requests.
     */
    @GetMapping("/all")
    @Operation(summary = "Get all adoption requests", description = "Returns a list of all adoption requests")
    @Schema(description = "Get all adoption requests")
    public ResponseEntity<List<AdoptionRequestDTO>> getAllAdoptionRequests() {
        return new ResponseEntity<>(adoptionRequestService.getAllAdoptionRequests(), HttpStatus.OK);
    }

    /**
     * Endpoint to get an adoption request by id.
     *
     * @param id The id of the adoption request.
     * @return The AdoptionRequestDTO object.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get an adoption request by id", description = "Returns an adoption request with the specified id")
    @Schema(description = "Get an adoption request by id")
    public ResponseEntity<AdoptionRequestDTO> getAdoptionRequestById(@PathVariable Long id) {
        return new ResponseEntity<>(adoptionRequestService.getAdoptionRequestById(id), HttpStatus.OK);
    }

    /**
     * Endpoint to delete an adoption request by id.
     *
     * @param id The id of the adoption request.
     * @return The deleted adoption request.
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete an adoption request by id", description = "Deletes an adoption request with the specified id")
    @Schema(description = "Delete an adoption request by id")
    public ResponseEntity<AdoptionRequestDTO> deleteAdoptionRequestById(@PathVariable Long id) {
        adoptionRequestService.deleteAdoptionRequestById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    /**
     * Endpoint to get all request details of an adoption request.
     *
     * @param id The id of the adoption request.
     * @return The list of request details.
     */
    @GetMapping("/{id}/details")
    @Operation(summary = "Get all request details of an adoption request", description = "Returns a list of request details for an adoption request")
    @Schema(description = "Get all request details of an adoption request")
    public ResponseEntity<List<RequestDetailDTO>> getAllRequestDetails(@PathVariable Long id) {
        return new ResponseEntity<>(adoptionRequestService.getAllRequestDetails(id), HttpStatus.OK);
    }

    /**
     * Endpoint to create a new request detail.
     *
     * @param id                       The id of the adoption request.
     * @param requestDetailCreationDTO The RequestDetailCreationDTO object.
     * @return The created RequestDetailDTO object.
     */
    @PostMapping("/{id}/detail-request")
    @Operation(summary = "Create a new request detail", description = "Creates a new request detail for an adoption request")
    @Schema(description = "Create a new request detail")
    public ResponseEntity<RequestDetailDTO> createRequestDetail(@PathVariable Long id, @RequestBody RequestDetailCreationDTO requestDetailCreationDTO) {
        return new ResponseEntity<>(adoptionRequestService.createRequestDetail(id, requestDetailCreationDTO), HttpStatus.CREATED);
    }

    /**
     * Endpoint to get a request detail by id.
     *
     * @param id       The id of the adoption request.
     * @param detailId The id of the request detail.
     * @return The RequestDetailDTO object.
     */
    @GetMapping("/{id}/detail-request/{detailId}")
    @Operation(summary = "Get a request detail by id", description = "Returns a request detail with the specified id for an adoption request")
    @Schema(description = "Get a request detail by id")
    public ResponseEntity<RequestDetailDTO> getRequestDetailById(@PathVariable Long id, @PathVariable Long detailId) {
        return new ResponseEntity<>(adoptionRequestService.getRequestDetailById(id, detailId), HttpStatus.OK);
    }
}
