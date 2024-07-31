package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.formsDTO.AdoptionFormCreateDTO;
import org.mindera.fur.code.dto.formsDTO.AdoptionFormDTO;
import org.mindera.fur.code.dto.formsDTO.FormFieldDTO;
import org.mindera.fur.code.mapper.AdoptionFormMapper;
import org.mindera.fur.code.model.form.AdoptionForm;
import org.mindera.fur.code.model.form.FormField;
import org.mindera.fur.code.repository.AdoptionFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class AdoptionFormService {

    private AdoptionFormRepository adoptionFormRepository;

    @Autowired
    public AdoptionFormService(AdoptionFormRepository AdoptionFormRepository) {
        this.adoptionFormRepository = AdoptionFormRepository;
    }


    public List<AdoptionFormDTO> getAll() {
        List<AdoptionForm> adoptionForms = adoptionFormRepository.findAll();
        return AdoptionFormMapper.INSTANCE.toDTOList(adoptionForms);
    }


    public AdoptionFormDTO getById(Long id) {
        AdoptionForm adoptionForm = adoptionFormRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("error"));
        return AdoptionFormMapper.INSTANCE.toDTO(adoptionForm);
    }

    public AdoptionFormDTO addAdoptionForm(AdoptionFormCreateDTO adoptionFormDto) {
        AdoptionForm adoptionForm = AdoptionFormMapper.INSTANCE.toModel(adoptionFormDto);
        adoptionFormRepository.save(adoptionForm);
        return AdoptionFormMapper.INSTANCE.toDTO(adoptionForm);
    }

    public AdoptionFormDTO delete(Long id) {
        AdoptionForm adoptionForm = adoptionFormRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("error"));
        adoptionFormRepository.delete(adoptionForm);
        return AdoptionFormMapper.INSTANCE.toDTO(adoptionForm);
    }


    public AdoptionFormDTO updateAdoptionForm(AdoptionFormDTO adoptionFormDto, Long id) {
        AdoptionForm existingForm = adoptionFormRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("error"));

        existingForm.setName(adoptionFormDto.getName());
        existingForm.setUpdatedAt(new Date());

        Set<FormField> existingFormFormFields = existingForm.getFormFields();
        Set<FormFieldDTO> newFieldsDTO = adoptionFormDto.getFormFields();

        /*              Remover fields que não estão mais na lista de novos campos              */
        existingFormFormFields.removeIf(field -> newFieldsDTO.stream().noneMatch(dto -> dto.getId().equals(field.getId())));

        for (FormFieldDTO fieldDTO : newFieldsDTO) {
            FormField field = existingFormFormFields.stream()
                    .filter(f -> fieldDTO.getId() != null && f.getId().equals(fieldDTO.getId()))
                    .findFirst()
                    .orElse(new FormField());

            field.setForm(existingForm);
            field.setName(fieldDTO.getQuestion());
            field.setType(fieldDTO.getType());
            //   field.setLabel(fieldDTO.getAnswer());


            /*              Se o campo não existir, adicionar ao form              */
            if (field.getId() == null) {
                existingFormFormFields.add(field);
            }
        }
        existingForm.setFormFields(existingFormFormFields);
        AdoptionForm savedForm = adoptionFormRepository.save(existingForm);


        return AdoptionFormMapper.INSTANCE.toDTO(savedForm);
    }


}
