package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailCreationDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailDTO;
import org.mindera.fur.code.exceptions.adoptionRequest.AdoptionRequestNotFound;
import org.mindera.fur.code.mapper.AdoptionRequestMapper;
import org.mindera.fur.code.mapper.RequestDetailMapper;
import org.mindera.fur.code.messages.adoptionRequest.AdoptionRequestMessage;
import org.mindera.fur.code.model.AdoptionRequest;
import org.mindera.fur.code.model.RequestDetail;
import org.mindera.fur.code.repository.AdoptionRequestRepository;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.RequestDetailRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    public AdoptionRequestService(AdoptionRequestRepository adoptionRequestRepository,
                                  PetRepository petRepository,
                                  ShelterRepository shelterRepository,
                                  PersonRepository personRepository,
                                  RequestDetailService requestDetailService,
                                  RequestDetailRepository requestDetailRepository) {
        this.adoptionRequestRepository = adoptionRequestRepository;
        this.petRepository = petRepository;
        this.shelterRepository = shelterRepository;
        this.personRepository = personRepository;
        this.requestDetailService = requestDetailService;
        this.requestDetailRepository = requestDetailRepository;
    }

    private static void idValidation(Long id) {
        if (id == null) {
            throw new AdoptionRequestNotFound(AdoptionRequestMessage.ADOPTION_REQUEST_ID_CANT_BE_EMPTY);
        }
        if (id <= 0) {
            throw new AdoptionRequestNotFound(AdoptionRequestMessage.ADOPTION_REQUEST_ID_CANT_BE_ZERO_OR_LOWER);
        }
    }

    public AdoptionRequestDTO createAdoptionRequest(AdoptionRequestCreationDTO dto) {
        System.out.println("Service received DTO: " + dto);
        AdoptionRequest request = new AdoptionRequest();
        request.setPet(petRepository.findById(dto.getPetId()).orElseThrow(() -> new RuntimeException("Pet not found")));
        request.setShelter(shelterRepository.findById(dto.getShelterId()).orElseThrow(() -> new RuntimeException("Shelter not found")));
        request.setPerson(personRepository.findById(dto.getPersonId()).orElseThrow(() -> new RuntimeException("Person not found")));
        System.out.println("AdoptionRequest before saving: " + request);
        AdoptionRequest savedRequest = adoptionRequestRepository.save(request);
        System.out.println("AdoptionRequest after saving: " + savedRequest);
        return adoptionRequestMapper.INSTANCE.toDTO(savedRequest);
    }

    public AdoptionRequestDTO updateAdoptionRequest(Long id, AdoptionRequestDTO adoptionRequestDTO) {
        idValidation(id);
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id).orElseThrow();
        AdoptionRequest updateAdoptionRequest = adoptionRequestMapper.INSTANCE.toModel(adoptionRequestDTO);
        updateAdoptionRequest.setShelter(adoptionRequest.getShelter());
        updateAdoptionRequest.setPet(adoptionRequest.getPet());
        adoptionRequestRepository.save(updateAdoptionRequest);
        return adoptionRequestMapper.INSTANCE.toDTO(updateAdoptionRequest);
    }

    public List<AdoptionRequestDTO> getAllAdoptionRequests() {
        List<AdoptionRequest> adoptionRequest = adoptionRequestRepository.findAll();
        return adoptionRequestMapper.INSTANCE.toDTO(adoptionRequest);
    }

    public AdoptionRequestDTO getAdoptionRequestById(Long id) {
        idValidation(id);
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id).orElseThrow();
        return adoptionRequestMapper.INSTANCE.toDTO(adoptionRequest);
    }

    public void deleteAdoptionRequestById(Long id) {
        idValidation(id);
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id).orElseThrow();
        adoptionRequestRepository.delete(adoptionRequest);
    }

    public void deleteAllAdoptionRequests() {
        adoptionRequestRepository.deleteAll();
    }


    public List<RequestDetailDTO> getAllRequestDetails(Long id) {
        idValidation(id);
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id).orElseThrow();
        List<RequestDetail> requestDetails = requestDetailRepository.findAllByAdoptionRequestId(adoptionRequest.getId());
        return requestDetailMapper.INSTANCE.toDTO(requestDetails);

    }

    public RequestDetailDTO createRequestDetail(Long id, RequestDetailCreationDTO requestDetailCreationDTO) {
        idValidation(id);
        adoptionRequestRepository.findById(id).orElseThrow(() -> new AdoptionRequestNotFound("AdoptionRequest not found"));
        return requestDetailService.createRequestDetail(id, requestDetailCreationDTO);
    }

    public RequestDetailDTO getRequestDetailById(Long id, Long detailId) {
        idValidation(id);
        adoptionRequestRepository.findById(id).orElseThrow();
        return requestDetailService.getRequestDetailById(detailId);
    }
}
