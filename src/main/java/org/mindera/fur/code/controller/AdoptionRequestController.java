package org.mindera.fur.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestUpdateDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailCreationDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailDTO;
import org.mindera.fur.code.service.AdoptionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling adoption request related requests.
 */
@Validated
@RestController
@RequestMapping("/api/v1/adoption-request")
@Tag(name = "Adoption Request Controller", description = "Endpoint for handling adoption requests")
public class AdoptionRequestController {

    private final AdoptionRequestService adoptionRequestService;

    /**
     * Constructor for the AdoptionRequestController
     *
     * @param adoptionRequestService the service that handles adoption request operations.
     */
    @Autowired
    public AdoptionRequestController(AdoptionRequestService adoptionRequestService) {
        this.adoptionRequestService = adoptionRequestService;
    }

    /**
     * Endpoint to create an adoption request.
     *
     * @param creationDto The AdoptionRequestCreationDTO object.
     * @return The created AdoptionRequestDTO object.
     */
    @Operation(summary = "Create an adoption request", description = "Creates a new adoption request with the provided data")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdoptionRequestDTO> createAdoptionRequest(@RequestBody @Valid AdoptionRequestCreationDTO creationDto) {
        AdoptionRequestDTO adoptionDto = adoptionRequestService.createAdoptionRequest(creationDto);
        return new ResponseEntity<>(adoptionDto, HttpStatus.CREATED);
    }

    /**
     * Endpoint to update an adoption request.
     *
     * @param id         The id of the adoption request.
     * @param updateDto The AdoptionRequestUpdateDTO object.
     * @return The updated AdoptionRequestDTO object.
     */
    @Operation(summary = "Update an adoption request", description = "Updates an adoption request with the provided data")
    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdoptionRequestDTO> updateAdoptionRequest(
            @PathVariable @NotNull @Positive Long id,
            @RequestBody @Valid AdoptionRequestUpdateDTO updateDto) {
        AdoptionRequestDTO adoptionDto = adoptionRequestService.updateAdoptionRequest(id, updateDto);
        return new ResponseEntity<>(adoptionDto, HttpStatus.OK);
    }

    /**
     * Endpoint to get all adoption requests.
     *
     * @return The list of all adoption requests.
     */
    @Operation(summary = "Get all adoption requests", description = "Returns a list of all adoption requests")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdoptionRequestDTO>> getAllAdoptionRequests() {
        List<AdoptionRequestDTO> adoptionDtoList = adoptionRequestService.getAllAdoptionRequests();
        return new ResponseEntity<>(adoptionDtoList, HttpStatus.OK);
    }

    /**
     * Endpoint to get an adoption request by id.
     *
     * @param id The id of the adoption request.
     * @return The AdoptionRequestDTO object.
     */
    @Operation(summary = "Get an adoption request by id", description = "Returns an adoption request with the specified id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdoptionRequestDTO> getAdoptionRequestById(@PathVariable @NotNull @Positive Long id) {
        AdoptionRequestDTO adoptionDto = adoptionRequestService.getAdoptionRequestById(id);
        return new ResponseEntity<>(adoptionDto, HttpStatus.OK);
    }

    /**
     * Endpoint to delete an adoption request by id.
     *
     * @param id The id of the adoption request.
     * @return The deleted adoption request.
     */
    @Operation(summary = "Delete an adoption request by id", description = "Deletes an adoption request with the specified id")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteAdoptionRequestById(@PathVariable @NotNull @Positive Long id) {
        adoptionRequestService.deleteAdoptionRequestById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    /**
//     * Endpoint to get all request details of an adoption request.
//     *
//     * @param id The id of the adoption request.
//     * @return The list of request details.
//     */
//    @Operation(summary = "Get all request details of an adoption request", description = "Returns a list of request details for an adoption request")
//    @GetMapping(value = "/{id}/details", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<RequestDetailDTO>> getAllRequestDetails(@PathVariable @NotNull @Positive Long id) {
//        List<RequestDetailDTO> detailDtoList = adoptionRequestService.getAllRequestDetails(id);
//        return new ResponseEntity<>(detailDtoList, HttpStatus.OK);
//    }

//    /**
//     * Endpoint to create a new request detail.
//     *
//     * @param id                       The id of the adoption request.
//     * @param creationDto The RequestDetailCreationDTO object.
//     * @return The created RequestDetailDTO object.
//     */
//    @Operation(summary = "Create a new request detail", description = "Creates a new request detail for an adoption request")
//    @PostMapping(value = "/{id}/detail-request", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<RequestDetailDTO> createRequestDetail(
//            @PathVariable @NotNull @Positive Long id,
//            @RequestBody @Valid RequestDetailCreationDTO creationDto) {
//        RequestDetailDTO detailDto = adoptionRequestService.createRequestDetail(id, creationDto);
//        return new ResponseEntity<>(detailDto, HttpStatus.CREATED);
//    }

//    /**
//     * Endpoint to get a request detail by id.
//     *
//     * @param id       The id of the adoption request.
//     * @param detailId The id of the request detail.
//     * @return The RequestDetailDTO object.
//     */
//    @Operation(summary = "Get a request detail by id", description = "Returns a request detail with the specified id for an adoption request")
//    @GetMapping("/{id}/detail-request/{detailId}")
//    public ResponseEntity<RequestDetailDTO> getRequestDetailById(
//            @PathVariable @NotNull @Positive Long id,
//            @PathVariable @NotNull @Positive Long detailId) {
//        RequestDetailDTO detailDto = adoptionRequestService.getRequestDetailById(id, detailId);
//        return new ResponseEntity<>(detailDto, HttpStatus.OK);
//    }
}
