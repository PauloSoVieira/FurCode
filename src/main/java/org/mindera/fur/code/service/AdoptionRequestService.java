package org.mindera.fur.code.service;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestUpdateDTO;
import org.mindera.fur.code.dto.form.FormDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailCreationDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailDTO;
import org.mindera.fur.code.exceptions.adoptionRequest.AdoptionRequestNotFound;
import org.mindera.fur.code.mapper.AdoptionRequestMapper;
import org.mindera.fur.code.mapper.RequestDetailMapper;
import org.mindera.fur.code.mapper.adoption_request.AdoptionRequestUpdateMapper;
import org.mindera.fur.code.model.AdoptionRequest;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.model.RequestDetail;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.form.Form;
import org.mindera.fur.code.model.pet.Pet;
import org.mindera.fur.code.repository.AdoptionRequestRepository;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.RequestDetailRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.form.FormRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.mindera.fur.code.service.form.FormService;
import org.mindera.fur.code.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Service class for handling AdoptionRequests.
 */
@Validated
@Service
public class AdoptionRequestService {

    private final AdoptionRequestRepository adoptionRequestRepository;
    private final RequestDetailService requestDetailService;
    private final RequestDetailRepository requestDetailRepository;
    private final FormService formService;
    private final FormRepository formRepository;
    private final PersonRepository personRepository;
    private final ShelterRepository shelterRepository;
    private final PetRepository petRepository;
    private final PetService petService;
    private final ShelterService shelterService;
    private final PersonService personService;

    /**
     * Constructor for the AdoptionRequestService.
     *
     * @param adoptionRequestRepository the adoptionRequestRepository
     * @param petRepository             the petRepository
     * @param shelterRepository         the shelterRepository
     * @param personRepository          the personRepository
     * @param requestDetailService      the requestDetailService
     * @param requestDetailRepository   the requestDetailRepository
     * @param formService               the formService
     * @param formRepository            the formRepository
     */
    @Autowired
    public AdoptionRequestService(
            AdoptionRequestRepository adoptionRequestRepository,
            PetRepository petRepository,
            ShelterRepository shelterRepository,
            PersonRepository personRepository,
            RequestDetailService requestDetailService,
            RequestDetailRepository requestDetailRepository,
            FormService formService,
            FormRepository formRepository,
            PetService petService,
            ShelterService shelterService,
            PersonService personService
    ) {
        this.adoptionRequestRepository = adoptionRequestRepository;
        this.petRepository = petRepository;
        this.shelterRepository = shelterRepository;
        this.personRepository = personRepository;
        this.requestDetailService = requestDetailService;
        this.requestDetailRepository = requestDetailRepository;
        this.formService = formService;
        this.formRepository = formRepository;
        this.petService = petService;
        this.shelterService = shelterService;
        this.personService = personService;
    }

    /**
     * Creates a new adoption request based on the provided data.
     * This method creates an adoption request, associates it with a pet, shelter, and person,
     * and creates a form from the "adoption-template".
     *
     * @param dto The DTO containing the data for creating the adoption request
     * @return DTO representing the created adoption request
     * @throws RuntimeException if the pet, shelter, person, or form is not found
     */
    @Transactional
    @Operation(summary = "Create a new adoption request", description = "Creates a new adoption request with associated pet, shelter, person, and form")
    public AdoptionRequestDTO createAdoptionRequest(@Valid AdoptionRequestCreationDTO dto) {
        AdoptionRequest request = new AdoptionRequest();

        request.setPet(findAndAssignPet(dto.getPetId()));
        request.setShelter(findAndAssignShelter(dto.getShelterId()));
        request.setPerson(findAndAssignPerson(dto.getPersonId()));

        FormDTO formDTO = formService.createFormFromTemplate("adoption-template");

        Form form = formRepository.findById(formDTO.getId())
                .orElseThrow(() -> new RuntimeException("Form not found after creation"));
        request.setForm(form);

        AdoptionRequest savedRequest = adoptionRequestRepository.save(request);
        return AdoptionRequestMapper.INSTANCE.toDTO(savedRequest);
    }

    /**
     * Updates an adoption request with the provided data.
     *
     * @param id                 the id
     * @param updateDto the adoption request dto
     * @return the updated adoption request dto
     */
    @Transactional
    @Operation(summary = "Update an adoption request", description = "Updates an adoption request with the provided data")
    public AdoptionRequestDTO updateAdoptionRequest(Long id, AdoptionRequestUpdateDTO updateDto) {

        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id).orElseThrow();

        AdoptionRequest updatedAdoptedRequest = AdoptionRequestUpdateMapper.INSTANCE.updateAdoptionRequestFromDto(updateDto, adoptionRequest);

        // AdoptionRequest updateAdoptionRequest = AdoptionRequestMapper.INSTANCE.toModel(adoptionRequestDTO);
        //updateAdoptionRequest.setRequestDetails(adoptionRequest.getRequestDetails());

        updatedAdoptedRequest = adoptionRequestRepository.save(updatedAdoptedRequest);
        return AdoptionRequestMapper.INSTANCE.toDTO(updatedAdoptedRequest);
    }

    /**
     * Gets all adoption requests.
     *
     * @return the list of adoption request dtos
     */
    @Operation(summary = "Get all adoption requests", description = "Returns a list of all adoption requests")
    public List<AdoptionRequestDTO> getAllAdoptionRequests() {
        List<AdoptionRequest> adoptionRequest = adoptionRequestRepository.findAll();
        return AdoptionRequestMapper.INSTANCE.toDTO(adoptionRequest);
    }

    /**
     * Gets an adoption request by id.
     *
     * @param id the id
     * @return the adoption request dto
     */
    @Operation(summary = "Get an adoption request by id", description = "Returns an adoption request with the specified id")
    public AdoptionRequestDTO getAdoptionRequestById(Long id) {
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id).orElseThrow();
        return AdoptionRequestMapper.INSTANCE.toDTO(adoptionRequest);
    }

    /**
     * Deletes an adoption request by id.
     *
     * @param id the id
     */
    @Transactional
    @Operation(summary = "Delete an adoption request by id", description = "Deletes an adoption request with the specified id")
    public void deleteAdoptionRequestById(Long id) {
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Adoption request with id %s not found", id)));

        adoptionRequestRepository.delete(adoptionRequest);
    }

    /**
     * Deletes all adoption requests.
     */
    @Operation(summary = "Delete all adoption requests", description = "Deletes all adoption requests")
    public void deleteAllAdoptionRequests() {
        adoptionRequestRepository.deleteAll();
    }

    /**
     * Gets all request details by id.
     *
     * @param id the id
     * @return the list of request detail dtos
     */
    @Operation(summary = "Get all request details by id", description = "Returns a list of request details for the specified adoption request")
    public List<RequestDetailDTO> getAllRequestDetails(Long id) {
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id).orElseThrow();
        List<RequestDetail> requestDetails = requestDetailRepository.findAllByAdoptionRequestId(adoptionRequest.getId());
        return requestDetails.stream().map(RequestDetailMapper.INSTANCE::toDTO).toList();
    }

    /**
     * Creates a request detail.
     *
     * @param id                       the id
     * @param requestDetailCreationDTO the request detail creation dto
     * @return the request detail dto
     */
    @Operation(summary = "Create a request detail", description = "Creates a new request detail for the specified adoption request")
    public RequestDetailDTO createRequestDetail(Long id, RequestDetailCreationDTO requestDetailCreationDTO) {
        adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new AdoptionRequestNotFound("AdoptionRequest not found"));

        return requestDetailService.createRequestDetail(id, requestDetailCreationDTO);
    }

    /**
     * Gets a request detail by id.
     *
     * @param id       the id
     * @param detailId the detail id
     * @return the request detail dto
     */
    @Operation(summary = "Get a request detail by id", description = "Returns a request detail with the specified id for the specified adoption request")
    public RequestDetailDTO getRequestDetailById(Long id, Long detailId) {
        adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new AdoptionRequestNotFound("AdoptionRequest not found"));

        return requestDetailService.getRequestDetailById(detailId);
    }

    private Pet findAndAssignPet(Long id) {
        return petService.findActivePetEntityById(id);
    }

    private Shelter findAndAssignShelter(Long id) {
        return shelterService.findActiveShelterEntityById(id);
    }

    private Person findAndAssignPerson(Long id) {
        return personService.getPersonEntityById(id);
    }
}
