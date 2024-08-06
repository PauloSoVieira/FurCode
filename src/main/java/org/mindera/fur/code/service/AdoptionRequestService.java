package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.mapper.AdoptionRequestMapper;
import org.mindera.fur.code.model.AdoptionRequest;
import org.mindera.fur.code.repository.AdoptionRequestRepository;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionRequestService {

    @Autowired
    public AdoptionRequestRepository adoptionRequestRepository;


    //TODO CONSTRUCTOR URGENT
    public PetRepository petRepository;

    public ShelterRepository shelterRepository;

    public PersonRepository personRepository;

    public AdoptionRequestMapper adoptionRequestMapper;

    public AdoptionRequestDTO createAdoptionRequest(AdoptionRequestCreationDTO dto) {
        System.out.println("Service received DTO: " + dto);
        AdoptionRequest request = new AdoptionRequest();
        request.setPet(petRepository.findById(dto.getPetId()).orElseThrow(() -> new RuntimeException("Pet not found")));
        request.setShelter(shelterRepository.findById(dto.getShelterId()).orElseThrow(() -> new RuntimeException("Shelter not found")));
        request.setPerson(personRepository.findById(dto.getPersonId()).orElseThrow(() -> new RuntimeException("Person not found")));
        request.setState(dto.getState());
        request.setDate(dto.getDate());
        System.out.println("AdoptionRequest before saving: " + request);
        AdoptionRequest savedRequest = adoptionRequestRepository.save(request);
        System.out.println("AdoptionRequest after saving: " + savedRequest);
        return adoptionRequestMapper.INSTANCE.toDTO(savedRequest);
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

    public void deleteAllAdoptionRequests() {
        adoptionRequestRepository.deleteAll();
    }
}
