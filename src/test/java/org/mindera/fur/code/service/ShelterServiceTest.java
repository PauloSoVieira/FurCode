/*package org.mindera.fur.code.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.mapper.shelter.ShelterMapper;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShelterServiceTest {

    @Mock
    private ShelterRepository repository;

    @InjectMocks
    private ShelterService service;

    @Nested
    class CrudShelter {

        @Test
        void createShelter() {
            ShelterCreationDTO dto = new ShelterCreationDTO();
            dto.setName("test");

            Shelter shelter = ShelterMapper.INSTANCE.toModel(dto);

            repository.save(shelter);

            verify(repository, times(1)).save(shelter);
        }


        @Test
        void deleteShelter() {
//            Shelter shelter = new Shelter();
            repository.deleteAll();

            verify(repository, times(1)).deleteAll();
        }


        @Test
        void getShelterById() {
            Shelter shelter = new Shelter();
            shelter.setId(1L);
            when(repository.findById(1L)).thenReturn(Optional.of(shelter));

            ShelterMapper.INSTANCE.toDto(shelter);
            service.getShelterById(shelter.getId());

            verify(repository, times(1)).findById(1L);
        }

        @Test
        void getAllShelters() {
            Shelter shelter = new Shelter();
            when(repository.findAll()).thenReturn(List.of(shelter));

            service.getAllShelters();
            verify(repository, times(1)).findAll();
        }

        @Test
        void updateShelter() {
            Shelter shelter = new Shelter();
            shelter.setId(1L);
            when(repository.findById(1L)).thenReturn(Optional.of(shelter));

            ShelterDTO shelterUpdate = ShelterMapper.INSTANCE.toDto(shelter);
            shelterUpdate.setName("test");

            service.updateShelter(1L, shelterUpdate);

            verify(repository, times(1)).save(shelter);
        }

        @Test
        void deleteShelterById() {
            Shelter shelter = new Shelter();
            shelter.setId(1L);
            when(repository.findById(1L)).thenReturn(Optional.of(shelter));

            service.deleteShelter(1L);

            verify(repository, times(1)).delete(shelter);
        }

    }*/

