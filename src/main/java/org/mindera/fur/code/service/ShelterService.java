package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.dto.shelter.ShelterThemeDTO;
import org.mindera.fur.code.mapper.ShelterMapper;
import org.mindera.fur.code.model.Pet;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.ShelterTheme;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.PetRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.ShelterThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShelterService {

    private ShelterRepository shelterRepository;
    private PersonRepository personRepository;
    private PetRepository petRepository;
    private ShelterThemeRepository shelterThemeRepository;

    private ShelterMapper shelterMapper;

    @Autowired
    public ShelterService(ShelterRepository shelterRepository, PersonRepository personRepository, PetRepository petRepository, ShelterThemeRepository shelterThemeRepository) {
        this.shelterRepository = shelterRepository;
        this.personRepository = personRepository;
        this.petRepository = petRepository;
        this.shelterThemeRepository = shelterThemeRepository;
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
        shelterRepository.save(shelter);
        return shelterMapper.INSTANCE.toDto(shelter);
    }

    public void deleteAllShelters() {
        shelterRepository.deleteAll();
    }

    public void addPetToShelter(Long shelterId, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow();
        pet.setShelter(shelterRepository.findById(shelterId)
                .orElseThrow());
        petRepository.save(pet);
    }

    public List<ShelterThemeDTO> getAllShelterThemes() {
        return shelterThemeRepository.findAll().stream()
                .map(st -> shelterMapper.INSTANCE.toDto(st))
                .toList();
    }

    public ShelterThemeDTO getShelterThemeById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Shelter Theme ID must be provided");
        }
        ShelterTheme theme = shelterThemeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shelter theme not found"));
        return shelterMapper.INSTANCE.toDto(theme);
    }

    public ShelterDTO changeShelterTheme(Long id, Long themeId) {
        if (id == null) {
            throw new IllegalArgumentException("Shelter ID must be provided");
        }
        if (themeId == null) {
            throw new IllegalArgumentException("Shelter Theme ID must be provided");
        }
        Shelter shelter = shelterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shelter not found"));
        ShelterTheme theme = shelterThemeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("Shelter theme not found"));
        shelter.setShelterTheme(theme);
        shelterRepository.save(shelter);
        return shelterMapper.INSTANCE.toDto(shelter);
    }

   /* public List<Request> getAllRequests() {
        List<Person> persons = personRepository.findAll();
        return persons.stream()
                .filter(person -> person.getShelter().getId().equals(personId))
                .map(this::convertToPersonDTO)
                .collect(Collectors.toList());
    }

    public void requestAdoption(Long id, Long petId, Long personId) {
        Shelter shelter = shelterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shelter not found"));
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person not found"));
        pet.setShelter(shelter);
        person.setShelter(shelter);
        person.setPet(pet);
        shelter.getPets().add(pet);
        shelter.getPeople().add(person);
        petRepository.save(pet);
        personRepository.save(person);
        shelterRepository.save(shelter);
    }*/
}
