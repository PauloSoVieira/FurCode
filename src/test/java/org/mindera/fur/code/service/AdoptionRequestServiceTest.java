package org.mindera.fur.code.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.mapper.AdoptionRequestMapper;
import org.mindera.fur.code.model.AdoptionRequest;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.pet.Pet;
import org.mindera.fur.code.repository.AdoptionRequestRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdoptionRequestServiceTest {

    Date date = new Date();
    Pet pet = new Pet();
    Person person = new Person();
    Shelter shelter = new Shelter();

    @Mock
    private AdoptionRequestRepository repository;
    @InjectMocks
    private AdoptionRequestService service;

    @Nested
    class CrudAdoptionRequest {

        @Test
        void createAdoptionRequest() {
            AdoptionRequestCreationDTO dto = new AdoptionRequestCreationDTO();
            dto.setPetId(1L);
            dto.setShelterId(1L);
            dto.setPersonId(1L);

            AdoptionRequest adoptionRequest = AdoptionRequestMapper.INSTANCE.toModel(dto);

            repository.save(adoptionRequest);

            verify(repository, times(1)).save(adoptionRequest);
        }

        @Test
        void getAllAdoptionRequests() {
            AdoptionRequest adoptionRequest = new AdoptionRequest();
            when(repository.findAll()).thenReturn(List.of(adoptionRequest));

            service.getAllAdoptionRequests();
            verify(repository, times(1)).findAll();
        }

        @Test
        void getAdoptionRequestById() {
            AdoptionRequest adoptionRequest = new AdoptionRequest();
            adoptionRequest.setId(1L);
            when(repository.findById(1L)).thenReturn(Optional.of(adoptionRequest));

            AdoptionRequestMapper.INSTANCE.toDTO(adoptionRequest);
            service.getAdoptionRequestById(adoptionRequest.getId());

            verify(repository, times(1)).findById(1L);
        }

        @Test
        void deleteAdoptionRequestById() {
            AdoptionRequest adoptionRequest = new AdoptionRequest();
            adoptionRequest.setId(1L);
            when(repository.findById(1L)).thenReturn(Optional.of(adoptionRequest));

            service.deleteAdoptionRequestById(adoptionRequest.getId());

            verify(repository, times(1)).delete(adoptionRequest);
        }

        @Test
        void deleteAllAdoptionRequests() {
            repository.deleteAll();

            verify(repository, times(1)).deleteAll();
        }

//        @Test
//        void updateAdoptionRequest() {
//            AdoptionRequest adoptionRequest = new AdoptionRequest();
//            adoptionRequest.setId(1L);
//            pet.setId(1L);
//            adoptionRequest.setPet(pet);
//            shelter.setId(1L);
//            adoptionRequest.setShelter(shelter);
//            person.setId(1L);
//            adoptionRequest.setPerson(person);
//            when(repository.findById(1L)).thenReturn(Optional.of(adoptionRequest));
//
//            AdoptionRequestDTO adoptionRequestUpdate = AdoptionRequestMapper.INSTANCE.toDTO(adoptionRequest);
//            adoptionRequestUpdate.setPetId(1L);
//            adoptionRequestUpdate.setShelterId(1L);
//            adoptionRequestUpdate.setPersonId(1L);
//
//            service.updateAdoptionRequest(adoptionRequest.getId(), adoptionRequestUpdate);
//
//            verify(repository, times(1)).save(adoptionRequest);
//        }
    }
}