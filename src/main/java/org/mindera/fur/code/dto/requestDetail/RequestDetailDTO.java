package org.mindera.fur.code.dto.requestDetail;

import lombok.Data;
import org.mindera.fur.code.model.State;

import java.util.Date;

@Data
public class RequestDetailDTO {
    private Long id;
    private Long personId;
    private State state;
    private Date date;
    private String observation;

    //@Autowired
    //private AdoptionRequestService adoptionRequestService;

    public RequestDetailDTO(Long id, Long personId, State state, Date date, String observation) {
        this.id = id;
        this.personId = personId;
        this.state = state;
        this.date = date;
        this.observation = observation;
    }

    //public AdoptionRequestDTO getAdoptionRequestById(Long id) {
    //    return adoptionRequestService.getAdoptionRequestById(id);
    //}
}
