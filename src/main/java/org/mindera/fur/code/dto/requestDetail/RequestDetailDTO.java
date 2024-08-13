package org.mindera.fur.code.dto.requestDetail;

import lombok.Data;
import org.mindera.fur.code.model.State;

import java.util.Date;

/**
 * Class containing the data of the RequestDetailDTO.
 */
@Data
public class RequestDetailDTO {
    private Long id;
    private Long personId;
    private State state;
    private Date date;
    private String observation;

    //@Autowired
    //private AdoptionRequestService adoptionRequestService;

    /**
     * Constructor with parameters.
     *
     * @param id          The id of the RequestDetail.
     * @param personId    The id of the person.
     * @param state       The state of the RequestDetail.
     * @param date        The date of the RequestDetail.
     * @param observation The observation of the RequestDetail.
     */
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
