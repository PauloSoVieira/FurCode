
package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.forms.AdoptionFormCreateDTO;
import org.mindera.fur.code.dto.forms.AdoptionFormDTO;
import org.mindera.fur.code.exceptions.adoptionFormException.AdoptionFormNotFound;
import org.mindera.fur.code.mapper.adoptionMapper.AdoptionFormMapper;
import org.mindera.fur.code.model.form.AdoptionForm;
import org.mindera.fur.code.repository.AdoptionFormRepository;
import org.mindera.fur.code.repository.FormFieldRepository2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Service class for managing AdoptionForms.
 */
@Service
public class AdoptionFormService {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionFormService.class);
    private AdoptionFormRepository adoptionFormRepository;
    private FormFieldRepository2 formFieldRepository2;

    /**
     * Constructor for Dependency Injection.
     *
     * @param AdoptionFormRepository the AdoptionForm repository.
     */
    @Autowired
    public AdoptionFormService(AdoptionFormRepository AdoptionFormRepository, FormFieldRepository2 formFieldRepository2) {
        this.adoptionFormRepository = AdoptionFormRepository;
        this.formFieldRepository2 = formFieldRepository2;
    }

    /**
     * Retrieves all adoption forms.
     *
     * @return a list of AdoptionFormDTOs.
     */
    public List<AdoptionFormDTO> getAll() {
        List<AdoptionForm> adoptionForms = adoptionFormRepository.findAll();
        return AdoptionFormMapper.INSTANCE.toDTOList(adoptionForms);
    }

    /**
     * Creates a new adoption form.
     *
     * @param adoptionFormDto the DTO for creating a new adoption form.
     * @return the created AdoptionFormDTO.
     */

    public AdoptionFormDTO createAdoptionForm(AdoptionFormCreateDTO adoptionFormDto) {
        validateAdoptionFormCreateDTO(adoptionFormDto);
        AdoptionForm adoptionForm = AdoptionFormMapper.INSTANCE.toModel(adoptionFormDto);

        adoptionFormRepository.save(adoptionForm);
        return AdoptionFormMapper.INSTANCE.toDTO(adoptionForm);
    }

    /**
     * Deletes an adoption form by ID.
     *
     * @param id the ID of the adoption form to be deleted.
     * @return the deleted AdoptionFormDTO.
     */

    public AdoptionFormDTO delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        AdoptionForm adoptionForm = adoptionFormRepository.findById(id)
                .orElseThrow(() -> new AdoptionFormNotFound("Adoption form not found for ID: " + id));

        adoptionFormRepository.delete(adoptionForm);


        return AdoptionFormMapper.INSTANCE.toDTO(adoptionForm);
    }

    /**
     * Updates an existing adoption form.
     *
     * @param adoptionFormDto the DTO containing updated information.
     * @param id              the ID of the adoption form to be updated.
     * @return the updated AdoptionFormDTO.
     */

    public AdoptionFormDTO updateAdoptionForm(AdoptionFormDTO adoptionFormDto, Long id) {

        AdoptionForm adoptionForm = adoptionFormRepository.findById(id).orElseThrow(() -> new AdoptionFormNotFound("Adoption form not found for ID: " + id));

        AdoptionForm updatedForm = AdoptionFormMapper.INSTANCE.toModel(adoptionFormDto);

        adoptionForm.setName(updatedForm.getName());
        adoptionForm.setUpdatedAt(new Date());
        adoptionForm.setFormField1s(updatedForm.getFormField1s());
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

    /**
     * Retrieves an adoption form by ID.
     *
     * @param id the ID of the adoption form.
     * @return the retrieved AdoptionFormDTO.
     */

    public AdoptionFormDTO getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        AdoptionForm adoptionForm = adoptionFormRepository.findById(id)
                .orElseThrow(() -> new AdoptionFormNotFound("Adoption form not found for ID: " + id));
        adoptionForm.setFormField1s(formFieldRepository2.findAllByAdoptionFormId(adoptionForm.getId()));
        return AdoptionFormMapper.INSTANCE.toDTO(adoptionForm);
    }

    /**
     * Deletes all adoption forms.
     */

    public void deleteAllAdoptionForms() {
        adoptionFormRepository.deleteAll();
        ;
    }


    /**
     * Validates the AdoptionFormCreateDTO.
     *
     * @param adoptionFormDto the DTO to be validated.
     */
    private void validateAdoptionFormCreateDTO(AdoptionFormCreateDTO adoptionFormDto) {
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
    }
}
