package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.MedicalRecordDTO;
import org.mindera.fur.code.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetService {

    PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }


    public MedicalRecordDTO getMedicalRecord(Long id) {
        return null;
    }

}
