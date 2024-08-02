package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.formsDTO.AdoptionFormCreateDTO;
import org.mindera.fur.code.dto.formsDTO.AdoptionFormDTO;
import org.mindera.fur.code.exceptions.adoptionFormException.AdoptionFormNotFound;
import org.mindera.fur.code.mapper.adoptionMapper.AdoptionFormMapper;
import org.mindera.fur.code.model.form.AdoptionForm;
import org.mindera.fur.code.repository.AdoptionFormRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AdoptionFormService {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionFormService.class);
    private AdoptionFormRepository adoptionFormRepository;


    @Autowired
    public AdoptionFormService(AdoptionFormRepository AdoptionFormRepository) {
        this.adoptionFormRepository = AdoptionFormRepository;
    }


    public List<AdoptionFormDTO> getAll() {
        List<AdoptionForm> adoptionForms = adoptionFormRepository.findAll();
        return AdoptionFormMapper.INSTANCE.toDTOList(adoptionForms);
    }


    public AdoptionFormDTO createAdoptionForm(AdoptionFormCreateDTO adoptionFormDto) {
        if (adoptionFormDto == null) {
            throw new IllegalArgumentException("Adoption form DTO cannot be null");
        }

        if (adoptionFormDto.getName() == null || adoptionFormDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (adoptionFormDto.getShelterId() == null) {
            throw new IllegalArgumentException("Shelter ID is required");
        }

        if (adoptionFormDto.getPersonId() == null) {
            throw new IllegalArgumentException("Person ID is required");
        }

        if (adoptionFormDto.getPetId() == null) {
            throw new IllegalArgumentException("Pet ID is required");
        }

        AdoptionForm adoptionForm = AdoptionFormMapper.INSTANCE.toModel(adoptionFormDto);


        adoptionFormRepository.save(adoptionForm);

        return AdoptionFormMapper.INSTANCE.toDTO(adoptionForm);
    }

    public AdoptionFormDTO delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        AdoptionForm adoptionForm = adoptionFormRepository.findById(id)
                .orElseThrow(() -> new AdoptionFormNotFound("Adoption form not found for ID: " + id));

        adoptionFormRepository.delete(adoptionForm);


        return AdoptionFormMapper.INSTANCE.toDTO(adoptionForm);
    }


    public AdoptionFormDTO updateAdoptionForm(AdoptionFormDTO adoptionFormDto, Long id) {

        AdoptionForm adoptionForm = adoptionFormRepository.findById(id).orElseThrow(() -> new AdoptionFormNotFound("Adoption form not found for ID: " + id));

        AdoptionForm updatedForm = AdoptionFormMapper.INSTANCE.toModel(adoptionFormDto);

        adoptionForm.setName(updatedForm.getName());
        adoptionForm.setUpdatedAt(new Date());
        adoptionForm.setFormFields(updatedForm.getFormFields());
        adoptionFormRepository.save(adoptionForm);
        return AdoptionFormMapper.INSTANCE.toDTO(adoptionForm);



   /*     if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        if (adoptionFormDto == null) {
            throw new IllegalArgumentException("Adoption form DTO cannot be null");
        }

        // Verifica se os campos obrigatórios estão presentes
        if (adoptionFormDto.getName() == null || adoptionFormDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (adoptionFormDto.getFormFields() == null) {
            throw new IllegalArgumentException("Form fields are required");
        }

        AdoptionForm existingForm = adoptionFormRepository.findById(id)
                .orElseThrow(() -> new AdoptionFormNotFound("Adoption form not found for ID: " + id));

        existingForm.setName(adoptionFormDto.getName());
        existingForm.setUpdatedAt(new Date());

        Set<FormField> existingFormFormFields = existingForm.getFormFields();
        Set<FormFieldDTO> newFieldsDTO = adoptionFormDto.getFormFields();

        // Remover fields que não estão mais na lista de novos campos
        existingFormFormFields.removeIf(field -> newFieldsDTO.stream().noneMatch(dto -> dto.getId().equals(field.getId())));

        for (FormFieldDTO fieldDTO : newFieldsDTO) {
            FormField field = existingFormFormFields.stream()
                    .filter(f -> fieldDTO.getId() != null && f.getId().equals(fieldDTO.getId()))
                    .findFirst()
                    .orElse(new FormField());

            field.setForm(existingForm);
            field.setName(fieldDTO.getQuestion());
            field.setType(fieldDTO.getType());
            // field.setLabel(fieldDTO.getAnswer());

            // Se o campo não existir, adicionar ao form
            if (field.getId() == null) {
                existingFormFormFields.add(field);
            }
        }
        existingForm.setFormFields(existingFormFormFields);
        AdoptionForm savedForm = adoptionFormRepository.save(existingForm);


        return AdoptionFormMapper.INSTANCE.toDTO(savedForm);

    */
    }


    public AdoptionFormDTO getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        AdoptionForm adoptionForm = adoptionFormRepository.findById(id)
                .orElseThrow(() -> new AdoptionFormNotFound("Adoption form not found for ID: " + id));

        return AdoptionFormMapper.INSTANCE.toDTO(adoptionForm);
    }

    public void deleteAllAdoptionForms() {
        adoptionFormRepository.deleteAll();
        ;
    }
}
