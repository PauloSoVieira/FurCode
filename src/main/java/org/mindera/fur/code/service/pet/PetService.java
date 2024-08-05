package org.mindera.fur.code.service.pet;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.mindera.fur.code.dto.pet.PetCreateDTO;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.mindera.fur.code.mapper.PetMapper;
import org.mindera.fur.code.model.Pet;

import org.mindera.fur.code.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {
    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<PetDTO> findAllPets() {
        List<Pet> pets = petRepository.findAll();
        return pets.stream().map(PetMapper.INSTANCE::toDTO).toList();
    }

    public PetDTO findPetById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Pet ID must be provided");
        }
        Pet pet = petRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + id));
        return PetMapper.INSTANCE.toDTO(pet);
    }

    public PetDTO addPet(@Valid PetCreateDTO petCreateDTO) {
        Pet pet = PetMapper.INSTANCE.toModel(petCreateDTO);
        pet = petRepository.save(pet);
        return PetMapper.INSTANCE.toDTO(pet);
    }

    public PetDTO updatePet(Long id, PetCreateDTO petCreateDTO) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pet not found"));
        PetMapper.INSTANCE.updatePetFromDTO(petCreateDTO, pet);
        pet = petRepository.save(pet);
        return PetMapper.INSTANCE.toDTO(pet);
    }

    // TODO: The following method don't do the soft delete, but will be implemented in the future
    public void softDeletePet(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Pet ID must be provided");
        }
        Pet pet = petRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + id));
        petRepository.delete(pet);
    }

    // For testing purposes only
    public void deleteAllPets() {
        petRepository.deleteAll();
    }
}
