package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.mapper.AdoptionRequestMapper;
import org.mindera.fur.code.model.AdoptionRequest;
import org.mindera.fur.code.repository.AdoptionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionRequestService {

    @Autowired
    public AdoptionRequestRepository adoptionRequestRepository;

    public AdoptionRequestMapper adoptionRequestMapper;

    public AdoptionRequestDTO createAdoptionRequest(AdoptionRequestCreationDTO adoptionRequestCreationDTO) {
        AdoptionRequest adoptionRequest = adoptionRequestMapper.INSTANCE.toModel(adoptionRequestCreationDTO);
        adoptionRequestRepository.save(adoptionRequest);
        return adoptionRequestMapper.INSTANCE.toDTO(adoptionRequest);
    }

    public AdoptionRequestDTO updateAdoptionRequest(Long id, AdoptionRequestDTO adoptionRequestDTO) {
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
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id).orElseThrow();
        return adoptionRequestMapper.INSTANCE.toDTO(adoptionRequest);
    }

    public void deleteAdoptionRequestById(Long id) {
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id).orElseThrow();
        adoptionRequestRepository.delete(adoptionRequest);
    }
}
