package org.mindera.fur.code.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.mapper.ShelterMapper;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ShelterServiceTest {

    @Mock
    private ShelterRepository repository;

    @InjectMocks
    private ShelterService service;

    @Nested
    class GetShelter {

        @Test
        void createShelter() {
            ShelterCreationDTO dto = new ShelterCreationDTO();
            dto.setName("test");

            Shelter shelter = ShelterMapper.INSTANCE.toModel(dto);

            repository.save(shelter);

            verify(repository, times(1)).save(shelter);
        }
    }

    @Nested
    class DeleteShelter {

        @Test
        void deleteShelter() {
            repository.deleteAll();

            verify(repository, times(1)).deleteAll();
        }

    }

    @Nested
    class GetShelterById {

        @Test
        void getShelterById() {
            Shelter shelter = repository.findById(id).orElseThrow();
            
        }
    }
}