package org.mindera.fur.code.dto.adoptionRequest;

import lombok.Data;

@Data
public class AdoptionRequestDTO {
    private Long id;
    private Long shelterId;
    private Long personId;
    private Long petId;
    //private Set<RequestDetailDTO> requestDetails;

    public AdoptionRequestDTO(Long id, Long shelterId, Long personId, Long petId) {
        this.id = id;
        this.shelterId = shelterId;
        this.personId = personId;
        this.petId = petId;
        //this.requestDetails = requestDetails;
    }
}
