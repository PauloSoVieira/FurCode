package org.mindera.fur.code.service.pet;

import org.mindera.fur.code.dto.pet.PetCreateDTO;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.mindera.fur.code.mapper.PetMapper;
import org.mindera.fur.code.model.Pet;

import org.mindera.fur.code.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }


    public PetDTO createPet(PetCreateDTO petCreateDTO) {
        Pet pet = PetMapper.INSTANCE.toModel(petCreateDTO);
        pet = petRepository.save(pet);
        return PetMapper.INSTANCE.toDTO(pet);
    }
}
