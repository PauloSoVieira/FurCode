package org.mindera.fur.code.service;

import jakarta.transaction.Transactional;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.dto.form.FormDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailCreationDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailDTO;
import org.mindera.fur.code.exceptions.adoptionRequest.AdoptionRequestNotFound;
import org.mindera.fur.code.mapper.AdoptionRequestMapper;
import org.mindera.fur.code.mapper.RequestDetailMapper;
import org.mindera.fur.code.messages.adoptionRequest.AdoptionRequestMessage;
import org.mindera.fur.code.model.AdoptionRequest;
import org.mindera.fur.code.model.RequestDetail;
import org.mindera.fur.code.model.form.Form;
import org.mindera.fur.code.repository.AdoptionRequestRepository;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.RequestDetailRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.form.FormRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.mindera.fur.code.service.form.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Service class for handling AdoptionRequests.
 */
@Service
public class AdoptionRequestService {

    public AdoptionRequestRepository adoptionRequestRepository;
    private PetRepository petRepository;
    private AdoptionRequestMapper adoptionRequestMapper;
    private RequestDetailMapper requestDetailMapper;
    private ShelterRepository shelterRepository;
    private PersonRepository personRepository;
    private RequestDetailService requestDetailService;
    private RequestDetailRepository requestDetailRepository;
    private FormService formService;
    private FormRepository formRepository;

    /**
     * Constructor for the AdoptionRequestService.
     *
     * @param adoptionRequestRepository the adoptionRequestRepository
     * @param petRepository             the petRepository
     * @param shelterRepository         the shelterRepository
     * @param personRepository          the personRepository
     * @param requestDetailService      the requestDetailService
     * @param requestDetailRepository   the requestDetailRepository
     */
    @Autowired
    public AdoptionRequestService(AdoptionRequestRepository adoptionRequestRepository,
                                  PetRepository petRepository,
                                  ShelterRepository shelterRepository,
                                  PersonRepository personRepository,
                                  RequestDetailService requestDetailService,
                                  RequestDetailRepository requestDetailRepository, FormService formService, FormRepository formRepository) {
        this.adoptionRequestRepository = adoptionRequestRepository;
        this.petRepository = petRepository;
        this.shelterRepository = shelterRepository;
        this.personRepository = personRepository;
        this.requestDetailService = requestDetailService;
        this.requestDetailRepository = requestDetailRepository;
        this.formService = formService;
        this.formRepository = formRepository;
    }

    /**
     * Validates the id.
     *
     * @param id the id
     */
    private static void idValidation(Long id) {
        if (id == null) {
            throw new AdoptionRequestNotFound(AdoptionRequestMessage.ADOPTION_REQUEST_ID_CANT_BE_EMPTY);
        }
        if (id <= 0) {
            throw new AdoptionRequestNotFound(AdoptionRequestMessage.ADOPTION_REQUEST_ID_CANT_BE_ZERO_OR_LOWER);
        }
    }

    /**
     * Creates an adoption request.
     *
     * @param dto the adoption request creation dto
     * @return the adoption request dto
     */
    @Transactional
    public AdoptionRequestDTO createAdoptionRequest(AdoptionRequestCreationDTO dto) {
        AdoptionRequest request = new AdoptionRequest();
        request.setPet(petRepository.findById(dto.getPetId()).orElseThrow(() -> new RuntimeException("Pet not found")));
        request.setShelter(shelterRepository.findById(dto.getShelterId()).orElseThrow(() -> new RuntimeException("Shelter not found")));
        request.setPerson(personRepository.findById(dto.getPersonId()).orElseThrow(() -> new RuntimeException("Person not found")));

        try {
            FormDTO formDTO = formService.createFormFromTemplate("adoption-template");
            Form form = formRepository.findById(formDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Form not found after creation"));
            request.setForm(form);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create form for adoption request", e);
        }

        AdoptionRequest savedRequest = adoptionRequestRepository.save(request);
        return AdoptionRequestMapper.INSTANCE.toDTO(savedRequest);
    }


    /**
     * Updates an adoption request.
     *
     * @param id                 the id
     * @param adoptionRequestDTO the adoption request dto
     * @return the adoption request dto
     */
    public AdoptionRequestDTO updateAdoptionRequest(Long id, AdoptionRequestDTO adoptionRequestDTO) {
        idValidation(id);
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id).orElseThrow();
        AdoptionRequest updateAdoptionRequest = adoptionRequestMapper.INSTANCE.toModel(adoptionRequestDTO);
        updateAdoptionRequest.setShelter(adoptionRequest.getShelter());
        updateAdoptionRequest.setPet(adoptionRequest.getPet());
        adoptionRequestRepository.save(updateAdoptionRequest);
        return adoptionRequestMapper.INSTANCE.toDTO(updateAdoptionRequest);
    }

    /**
     * Gets all adoption requests.
     *
     * @return the list of adoption request dtos
     */
    public List<AdoptionRequestDTO> getAllAdoptionRequests() {
        List<AdoptionRequest> adoptionRequest = adoptionRequestRepository.findAll();
        return adoptionRequestMapper.INSTANCE.toDTO(adoptionRequest);
    }

    /**
     * Gets an adoption request by id.
     *
     * @param id the id
     * @return the adoption request dto
     */
    public AdoptionRequestDTO getAdoptionRequestById(Long id) {
        idValidation(id);
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id).orElseThrow();
        return adoptionRequestMapper.INSTANCE.toDTO(adoptionRequest);
    }

    /**
     * Deletes an adoption request by id.
     *
     * @param id the id
     */
    public void deleteAdoptionRequestById(Long id) {
        idValidation(id);
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id).orElseThrow();
        adoptionRequestRepository.delete(adoptionRequest);
    }

    /**
     * Deletes all adoption requests.
     */
    public void deleteAllAdoptionRequests() {
        adoptionRequestRepository.deleteAll();
    }

    /**
     * Gets all request details by id.
     *
     * @param id the id
     * @return the list of request detail dtos
     */
    public List<RequestDetailDTO> getAllRequestDetails(Long id) {
        idValidation(id);
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id).orElseThrow();
        List<RequestDetail> requestDetails = requestDetailRepository.findAllByAdoptionRequestId(adoptionRequest.getId());
        return requestDetailMapper.INSTANCE.toDTO(requestDetails);

    }

    /**
     * Creates a request detail.
     *
     * @param id
     * @param requestDetailCreationDTO
     * @return
     */
    public RequestDetailDTO createRequestDetail(Long id, RequestDetailCreationDTO requestDetailCreationDTO) {
        idValidation(id);
        adoptionRequestRepository.findById(id).orElseThrow(() -> new AdoptionRequestNotFound("AdoptionRequest not found"));
        return requestDetailService.createRequestDetail(id, requestDetailCreationDTO);
    }

    /**
     * Gets a request detail by id.
     *
     * @param id       the id
     * @param detailId the detail id
     * @return the request detail dto
     */
    public RequestDetailDTO getRequestDetailById(Long id, Long detailId) {
        idValidation(id);
        adoptionRequestRepository.findById(id).orElseThrow();
        return requestDetailService.getRequestDetailById(detailId);
    }
}
