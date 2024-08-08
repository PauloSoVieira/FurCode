package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.pet.PetDTO;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.mapper.ShelterMapper;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.mindera.fur.code.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShelterService {

    private PetService petService;

    private ShelterRepository shelterRepository;
    private PersonRepository personRepository;
    private PetRepository petRepository;

    private ShelterMapper shelterMapper;

    @Autowired
    public ShelterService(ShelterRepository shelterRepository,
                          PersonRepository personRepository,
                          PetRepository petRepository,
                          PetService petService
    ) {
        this.shelterRepository = shelterRepository;
        this.personRepository = personRepository;
        this.petRepository = petRepository;
        this.petService = petService;
    }

    public List<ShelterDTO> getAllShelters() {
        List<Shelter> shelters = shelterRepository.findAll();
        return shelterMapper.INSTANCE.toDto(shelters);
    }

    public ShelterDTO getShelterById(Long id) {
        Shelter shelter = shelterRepository.findById(id).orElseThrow();
        return shelterMapper.INSTANCE.toDto(shelter);
    }

    public ShelterDTO createShelter(ShelterCreationDTO shelterCreationDTO) {
        Shelter shelter = shelterMapper.INSTANCE.toModel(shelterCreationDTO);
        shelterRepository.save(shelter);
        return ShelterMapper.INSTANCE.toDto(shelter);
    }

    public ShelterDTO deleteShelter(Long id) {
        Shelter shelter = shelterRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Error"));
        shelterRepository.delete(shelter);
        return ShelterMapper.INSTANCE.toDto(shelter);
    }

    public ShelterDTO updateShelter(Long id, ShelterDTO shelterDTO) {
        Shelter shelter = shelterRepository.findById(id).orElseThrow();
        Shelter updateShelter = shelterMapper.INSTANCE.toModel(shelterDTO);
        shelter.setName(updateShelter.getName());
        shelter.setEmail(updateShelter.getEmail());
        shelter.setAddress1(updateShelter.getAddress1());
        shelter.setAddress2(updateShelter.getAddress2());
        shelter.setPostCode(updateShelter.getPostCode());
        shelter.setPhone(updateShelter.getPhone());
        shelter.setSize(updateShelter.getSize());
        shelter.setIsActive(updateShelter.getIsActive());
        shelter.setCreationDate(updateShelter.getCreationDate());
        shelterRepository.save(shelter);
        return shelterMapper.INSTANCE.toDto(shelter);
    }

    public void deleteAllShelters() {
        shelterRepository.deleteAll();
    }

    public List<PetDTO> getAllPetsInShelter(Long id) {
        return petService.findAllPets()
                .stream()
                .filter(pet -> pet
                        .getShelterId()
                        .equals(id))
                .toList();
    }
}
