package org.mindera.fur.code.service;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.mindera.fur.code.dto.requestDetail.RequestDetailCreationDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailDTO;
import org.mindera.fur.code.exceptions.person.PersonException;
import org.mindera.fur.code.exceptions.requestDetail.RequestDetailNotFound;
import org.mindera.fur.code.mapper.RequestDetailMapper;
import org.mindera.fur.code.messages.requestDetail.RequestDetailMessage;
import org.mindera.fur.code.model.AdoptionRequest;
import org.mindera.fur.code.model.RequestDetail;
import org.mindera.fur.code.model.State;
import org.mindera.fur.code.repository.AdoptionRequestRepository;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.RequestDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for handling RequestDetails.
 */
@Service
@Schema(description = "The request detail service")
@Tag(name = "Request Details", description = "Request Details")
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
            throw new RequestDetailNotFound(RequestDetailMessage.DETAIL_ID_CANT_BE_NULL);
        }
        if (id <= 0) {
            throw new PersonException(RequestDetailMessage.DETAIL_ID_CANT_BE_ZERO_OR_LOWER);
        }
        if (id.equals(" ")) {
            throw new RequestDetailNotFound(RequestDetailMessage.DETAIL_ID_CANT_BE_EMPTY);
        }
    }

    /**
     * Validates person id.
     *
     * @param personId
     */
    private static void personIdValidation(Long personId) {
        if (personId == null) {
            throw new RequestDetailNotFound(RequestDetailMessage.PERSON_ID_CANT_BE_NULL);
        }
        if (personId <= 0) {
            throw new RequestDetailNotFound(RequestDetailMessage.PERSON_ID_CANT_BE_ZERO_OR_LOWER);
        }
        if (personId.equals(" ")) {
            throw new RequestDetailNotFound(RequestDetailMessage.PERSON_ID_CANT_BE_EMPTY);
        }
    }

    /**
     * Validates state.
     *
     * @param state
     */
    private static void stateValidation(State state) {
        if (state == null) {
            throw new RequestDetailNotFound(RequestDetailMessage.STATE_CANT_BE_NULL);
        }
        if (state.equals(" ")) {
            throw new RequestDetailNotFound(RequestDetailMessage.STATE_CANT_BE_EMPTY);
        }
    }

    /**
     * Validates date.
     *
     * @param date
     */
    private static void dateValidation(LocalDate date) {
        if (date == null) {
            throw new RequestDetailNotFound(RequestDetailMessage.DATE_CANT_BE_NULL);
        }
        if (date.equals(" ")) {
            throw new RequestDetailNotFound(RequestDetailMessage.DATE_CANT_BE_EMPTY);
        }
        if (date.isAfter(LocalDate.now())) {
            throw new RequestDetailNotFound(RequestDetailMessage.DATE_CANT_BE_FUTURE);
        }
        if (date.isBefore(LocalDate.now())) {
            throw new RequestDetailNotFound(RequestDetailMessage.DATE_CANT_BE_PAST);
        }
    }

    /**
     * Validates observation.
     *
     * @param observation
     */
    private static void observationValidation(String observation) {
        if (observation.length() > 1000) {
            throw new RequestDetailNotFound(RequestDetailMessage.OBSERVATION_CANT_BE_LONGER_THAN_1000);
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
