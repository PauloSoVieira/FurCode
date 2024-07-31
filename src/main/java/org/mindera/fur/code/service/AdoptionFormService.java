package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.AdoptionFormCreateDTO;
import org.mindera.fur.code.dto.AdoptionFormDTO;
import org.mindera.fur.code.mapper.AdoptionFormMapper;
import org.mindera.fur.code.model.form.AdoptionForm;
import org.mindera.fur.code.repository.AdoptionFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionFormService {

    private AdoptionFormRepository AdoptionFormRepository;

    @Autowired
    public AdoptionFormService(AdoptionFormRepository AdoptionFormRepository) {
        this.AdoptionFormRepository = AdoptionFormRepository;
    }


    public List<AdoptionFormDTO> getAll() {
        List<AdoptionForm> adoptionForms = AdoptionFormRepository.findAll();
        return AdoptionFormMapper.INSTANCE.adoptionToAdoptionDto(adoptionForms);
    }

    public AdoptionFormDTO getById(Long id) {
        AdoptionForm adoptionForm = AdoptionFormRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("error"));
        return AdoptionFormMapper.INSTANCE.adoptionToDto(adoptionForm);
    }

    public AdoptionFormDTO addAdoptionForm(AdoptionFormCreateDTO adoptionFormDto) {
        AdoptionForm adoptionForm = AdoptionFormMapper.INSTANCE.adoptionToCreate(adoptionFormDto);
        AdoptionFormRepository.save(adoptionForm);
        return AdoptionFormMapper.INSTANCE.adoptionToDto(adoptionForm);
    }

    public AdoptionFormDTO delete(Long id) {
        AdoptionForm adoptionForm = AdoptionFormRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("error"));
        AdoptionFormRepository.delete(adoptionForm);
        return AdoptionFormMapper.INSTANCE.adoptionToDto(adoptionForm);
    }


    public AdoptionFormDTO updateAdoptionForm(AdoptionFormDTO adoptionFormDto, Long id) {
        AdoptionForm adoptionForm = AdoptionFormMapper.INSTANCE.adoptionToUpdate(adoptionFormDto);
        AdoptionFormRepository.save(adoptionForm);
        return AdoptionFormMapper.INSTANCE.adoptionToDto(adoptionForm);
    }
}
