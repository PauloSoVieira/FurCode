package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.ShelterDTO.ShelterDTO;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.model.Pet;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.repository.ShelterRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShelterService {

    @Autowired
    private ShelterRepository shelterRepository;

    public List<ShelterDTO> getAllShelters() {
        List<Shelter> shelters = shelterRepository.findAll();
        return shelters.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ShelterDTO getShelterById(Long id) {
        return shelterRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public ShelterDTO saveShelter(ShelterDTO shelterDTO) {
        Shelter shelter = convertToEntity(shelterDTO);
        shelter = shelterRepository.save(shelter);
        return convertToDTO(shelter);
    }

    public void deleteShelter(Long id) {
        shelterRepository.deleteById(id);
    }

    private ShelterDTO convertToDTO(Shelter shelter) {
        ShelterDTO shelterDTO = new ShelterDTO();
        BeanUtils.copyProperties(shelter, shelterDTO);
        return shelterDTO;
    }

    private Shelter convertToEntity(ShelterDTO shelterDTO) {
        Shelter shelter = new Shelter();
        BeanUtils.copyProperties(shelterDTO, shelter);
        return shelter;
    }

    public List<PersonDTO> getAllRequests(Long personId) {
        List<Person> persons = personRepository.findAll();
        return persons.stream()
                .filter(person -> person.getShelter().getId().equals(personId))
                .map(this::convertToPersonDTO)
                .collect(Collectors.toList());
    }

    public void addPersonToShelter(Long shelterId, Long personId, String role) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person not found"));
        person.setRole(role);
        person.setShelter(shelterRepository.findById(shelterId)
                .orElseThrow(() -> new IllegalArgumentException("Shelter not found")));
        personRepository.save(person);
    }

    public void addPetToShelter(Long shelterId, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        pet.setShelter(shelterRepository.findById(shelterId)
                .orElseThrow(() -> new IllegalArgumentException("Shelter not found")));
        petRepository.save(pet);
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
    }
}
