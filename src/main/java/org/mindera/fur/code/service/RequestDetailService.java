package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.requestDetail.RequestDetailCreationDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailDTO;
import org.mindera.fur.code.exceptions.person.PersonException;
import org.mindera.fur.code.exceptions.requestDetail.RequestDetailNotFound;
import org.mindera.fur.code.mapper.RequestDetailMapper;
import org.mindera.fur.code.messages.requestDetail.RequestDetailMessage;
import org.mindera.fur.code.model.AdoptionRequest;
import org.mindera.fur.code.model.RequestDetail;
import org.mindera.fur.code.repository.AdoptionRequestRepository;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.RequestDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for handling RequestDetails.
 */
@Service
public class RequestDetailService {

    private final RequestDetailRepository requestDetailRepository;
    private final PersonRepository personRepository;
    private final AdoptionRequestRepository adoptionRequestRepository;

    /**
     * Constructor for the RequestDetailService.
     *
     * @param requestDetailRepository   the requestDetailRepository
     * @param personRepository          the personRepository
     * @param adoptionRequestRepository the adoptionRequestRepository
     */
    @Autowired
    public RequestDetailService(RequestDetailRepository requestDetailRepository,
                                PersonRepository personRepository,
                                AdoptionRequestRepository adoptionRequestRepository) {
        this.requestDetailRepository = requestDetailRepository;
        this.personRepository = personRepository;
        this.adoptionRequestRepository = adoptionRequestRepository;
    }

    /**
     * Validates the id.
     *
     * @param id the id
     */
    private static void idValidation(Long id) {
        if (id == null) {
            throw new RequestDetailNotFound(RequestDetailMessage.DETAIL_ID_CANT_BE_EMPTY);
        }
        if (id <= 0) {
            throw new PersonException(RequestDetailMessage.DETAIL_ID_CANT_BE_ZERO_OR_LOWER);
        }
    }

    /**
     * Gets all request details.
     *
     * @return the list of request detail dtos
     */
    public List<RequestDetailDTO> getAllRequestDetails() {
        List<RequestDetail> requestDetails = requestDetailRepository.findAll();
        return RequestDetailMapper.INSTANCE.toDTO(requestDetails);
    }

    /**
     * Gets a request detail by id.
     *
     * @param id the id
     * @return the request detail dto
     */
    public RequestDetailDTO getRequestDetailById(Long id) {
        idValidation(id);
        RequestDetail requestDetail = requestDetailRepository.findById(id).orElseThrow();
        return RequestDetailMapper.INSTANCE.toDTO(requestDetail);
    }

    /**
     * Creates a request detail.
     *
     * @param id
     * @param requestDetailCreationDTO
     * @return
     */
    public RequestDetailDTO createRequestDetail(Long id, RequestDetailCreationDTO requestDetailCreationDTO) {
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id).orElseThrow();

        RequestDetail requestDetail = RequestDetailMapper.INSTANCE.toModel(requestDetailCreationDTO);

        requestDetail.setAdoptionRequest(adoptionRequest);

        requestDetailRepository.save(requestDetail);
        return RequestDetailMapper.INSTANCE.toDTO(requestDetail);
    }
}
