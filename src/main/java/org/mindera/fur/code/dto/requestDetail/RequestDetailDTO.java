package org.mindera.fur.code.dto.requestDetail;

import lombok.Data;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.model.State;
import org.mindera.fur.code.service.AdoptionRequestService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
public class RequestDetailDTO {
    private Long id;
    private Long personId;
    private State state;
    private Date date;
    private String observation;

    @Autowired
    private AdoptionRequestService adoptionRequestService;

    public RequestDetailDTO(Long personId, State state, Date date, String observation) {
        this.personId = personId;
        this.state = state;
        this.date = date;
        this.observation = observation;
    }

    public AdoptionRequestDTO getAdoptionRequestById(Long id) {
        return adoptionRequestService.getAdoptionRequestById(id);
    }
}
