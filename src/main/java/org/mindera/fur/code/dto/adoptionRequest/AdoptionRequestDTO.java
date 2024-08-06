package org.mindera.fur.code.dto.adoptionRequest;

import lombok.Data;
import org.mindera.fur.code.model.State;

import java.util.Date;

@Data
public class AdoptionRequestDTO {
    private Long id;
    private Long shelterId;
    private Long personId;
    private Long petId;
    private State state;
    private Date date;

    public AdoptionRequestDTO(Long shelterId, Long personId, Long petId, State state, Date date) {
        this.shelterId = shelterId;
        this.personId = personId;
        this.petId = petId;
        this.state = state;
        this.date = date;
    }
}
