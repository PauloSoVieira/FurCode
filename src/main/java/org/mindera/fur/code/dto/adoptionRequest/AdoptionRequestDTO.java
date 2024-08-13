package org.mindera.fur.code.dto.adoptionRequest;

import lombok.Data;

/**
 * DTO for an adoption request.
 */
@Data
public class AdoptionRequestDTO {
    private Long id;
    private Long shelterId;
    private Long personId;
    private Long petId;
    //private Set<RequestDetailDTO> requestDetails;

    /**
     * Constructor with parameters.
     *
     * @param id        The id of the adoption request.
     * @param shelterId The id of the shelter.
     * @param personId  The id of the person.
     * @param petId     The id of the pet.
     */
    public AdoptionRequestDTO(Long id, Long shelterId, Long personId, Long petId) {
        this.id = id;
        this.shelterId = shelterId;
        this.personId = personId;
        this.petId = petId;
        //this.requestDetails = requestDetails;
    }
}
