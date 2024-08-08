package org.mindera.fur.code.dto.adoptionRequest;

import lombok.Data;
import org.mindera.fur.code.dto.requestDetail.RequestDetailDTO;

import java.util.Set;

@Data
public class AdoptionRequestDTO {
    private Long id;
    private Long shelterId;
    private Long personId;
    private Long petId;
    private Set<RequestDetailDTO> requestDetails;

    public AdoptionRequestDTO(Long shelterId, Long personId, Long petId, Set<RequestDetailDTO> requestDetails) {
        this.shelterId = shelterId;
        this.personId = personId;
        this.petId = petId;
        this.requestDetails = requestDetails;
    }
}
