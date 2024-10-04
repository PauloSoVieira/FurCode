package org.mindera.fur.code.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestUpdateDTO;
import org.mindera.fur.code.dto.form.FormDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailCreationDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailDTO;
import org.mindera.fur.code.mapper.AdoptionRequestMapper;
import org.mindera.fur.code.mapper.RequestDetailMapper;
import org.mindera.fur.code.mapper.adoption_request.AdoptionRequestUpdateMapper;
import org.mindera.fur.code.messages.adoptionRequest.AdoptionRequestMessage;
import org.mindera.fur.code.model.AdoptionRequest;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.model.RequestDetail;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.form.Form;
import org.mindera.fur.code.model.pet.Pet;
import org.mindera.fur.code.repository.AdoptionRequestRepository;
import org.mindera.fur.code.repository.RequestDetailRepository;
import org.mindera.fur.code.repository.form.FormRepository;
import org.mindera.fur.code.service.form.FormService;
import org.mindera.fur.code.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
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
    private final ShelterService shelterService;
    private final PetService petService;
    private final PersonService personService;

    /**
     * Constructor for the AdoptionRequestService.
     *
     * @param adoptionRequestRepository the adoptionRequestRepository
     * @param shelterService            the shelterService
     * @param petService                the petService
     * @param personService             the personService
     * @param requestDetailService      the requestDetailService
     * @param requestDetailRepository   the requestDetailRepository
     * @param formService               the formService
     * @param formRepository            the formRepository
     */
    @Autowired
    public AdoptionRequestService(
            AdoptionRequestRepository adoptionRequestRepository,
            RequestDetailService requestDetailService,
            RequestDetailRepository requestDetailRepository,
            FormService formService,
            FormRepository formRepository,
            ShelterService shelterService,
            PetService petService,
            PersonService personService
    ) {
        this.adoptionRequestRepository = adoptionRequestRepository;
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
     * @param creationDto The DTO containing the data for creating the adoption request
     * @return DTO representing the created adoption request
     * @throws EntityNotFoundException if the pet, shelter, person, or form is not found
     */
//    @Caching(
//            evict = {
//                    @CacheEvict(cacheNames = "allAdoptionRequests", allEntries = true),
//            },
//            put = {
//                    @CachePut(cacheNames = "adoptionRequest", key = "#result.id")
//            }
//    )
    @Transactional
    public AdoptionRequestDTO createAdoptionRequest(@Valid AdoptionRequestCreationDTO creationDto) {
        AdoptionRequest adoptionRequest = buildAdoptionRequest(creationDto);
        AdoptionRequest savedRequest = adoptionRequestRepository.save(adoptionRequest);
        return AdoptionRequestMapper.INSTANCE.toDTO(savedRequest);
    }

    /**
     * Updates an adoption request with the provided data.
     *
     * @param id                 the id
     * @param updateDto the adoption request dto
     * @return the updated adoption request dto
     */
//    @Caching(
//            evict = {
//                    @CacheEvict(cacheNames = "allAdoptionRequests", allEntries = true),
//            },
//            put = {
//                    @CachePut(cacheNames = "adoptionRequest", key = "#id")
//            }
//    )
    @Transactional
    public AdoptionRequestDTO updateAdoptionRequest(@NotNull @Positive Long id, @Valid AdoptionRequestUpdateDTO updateDto) {
        AdoptionRequest adoptionRequest = findAndAssignAdoptionRequest(id);

        AdoptionRequest updatedAdoptedRequest = AdoptionRequestUpdateMapper.INSTANCE.updateAdoptionRequestFromDto(updateDto, adoptionRequest);

        updatedAdoptedRequest = adoptionRequestRepository.save(updatedAdoptedRequest);
        return AdoptionRequestMapper.INSTANCE.toDTO(updatedAdoptedRequest);
    }

    /**
     * Gets all adoption requests.
     *
     * @return the list of all adoption request.
     */
//    @Cacheable(cacheNames = "allAdoptionRequests")
    public List<AdoptionRequestDTO> getAllAdoptionRequests() {
        List<AdoptionRequest> adoptionRequest = adoptionRequestRepository.findAll();
        return AdoptionRequestMapper.INSTANCE.toDTO(adoptionRequest);
    }

    /**
     * Gets an adoption request by id.
     *
     * @param id the ID of the adoption request.
     * @return the adoption request dto
     */
//    @Cacheable(cacheNames = "adoptionRequest", key = "#id")
    public AdoptionRequestDTO getAdoptionRequestById(@NotNull @Positive Long id) {
        AdoptionRequest adoptionRequest = findAndAssignAdoptionRequest(id);
        return AdoptionRequestMapper.INSTANCE.toDTO(adoptionRequest);
    }

    /**
     * Deletes an adoption request by id.
     *
     * @param id the ID of the adoption request.
     */
//    @Caching(
//            evict = {
//                    @CacheEvict(cacheNames = "allAdoptionRequests", allEntries = true),
//                    @CacheEvict(cacheNames = "adoptionRequest", key = "#id")
//            }
//    )
    @Transactional
    public void deleteAdoptionRequestById(@NotNull @Positive Long id) {
        AdoptionRequest adoptionRequest = findAndAssignAdoptionRequest(id);
        adoptionRequestRepository.delete(adoptionRequest);
    }

    /**
     * Gets all request details by id.
     *
     * @param id the id
     * @return the list of request detail dtos
     */
//    @Cacheable(cacheNames = "requestDetails", key = "#id")
    public List<RequestDetailDTO> getAllRequestDetails(@NotNull @Positive Long id) {
        AdoptionRequest adoptionRequest = findAndAssignAdoptionRequest(id);
        List<RequestDetail> requestDetails = requestDetailRepository.findAllByAdoptionRequestId(adoptionRequest.getId());
        return RequestDetailMapper.INSTANCE.toDTO(requestDetails);
    }

//    /**
//     * Creates a request detail.
//     *
//     * @param id                       the id
//     * @param creationDTO the request detail creation dto
//     * @return the request detail dto
//     */
////    @CacheEvict(cacheNames = "requestDetails", key = "#id")
//    @Transactional
//    public RequestDetailDTO createRequestDetail(@NotNull @Positive Long id, @Valid RequestDetailCreationDTO creationDTO) {
//        findAndAssignAdoptionRequest(id);
//        return requestDetailService.createRequestDetail(id, creationDTO);
//    }

//    /**
//     * Gets a request detail by id.
//     *
//     * @param id       the id
//     * @param detailId the detail id
//     * @return the request detail dto
//     */
////    @Cacheable(cacheNames = "requestDetail", key = "#detailId")
//    public RequestDetailDTO getRequestDetailById(Long id, Long detailId) {
//        findAndAssignAdoptionRequest(id);
//        return requestDetailService.getRequestDetailById(detailId);
//    }

    // Helper methods
    private AdoptionRequest buildAdoptionRequest(AdoptionRequestCreationDTO creationDto) {
        AdoptionRequest adoptionRequest = new AdoptionRequest();
        adoptionRequest.setPet(findAndAssignPet(creationDto.getPetId()));
        adoptionRequest.setShelter(findAndAssignShelter(creationDto.getShelterId()));
        adoptionRequest.setPerson(findAndAssignPerson(creationDto.getPersonId()));
        adoptionRequest.setState(creationDto.getState());
        adoptionRequest.setDate(LocalDate.now());
        adoptionRequest.setForm(createAndAssignForm());
        return adoptionRequest;
    }

    private Form createAndAssignForm() {
        FormDTO formDTO = formService.createFormFromTemplate("adoption-template");
        return findAndAssignForm(formDTO.getId());
    }

    private AdoptionRequest findAndAssignAdoptionRequest(Long id) {
        return adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AdoptionRequestMessage.ADOPTION_REQUEST_NOT_FOUND + id));
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

    private Form findAndAssignForm(Long id) {
        return formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AdoptionRequestMessage.FORM_NOT_FOUND + id));
    }
}
