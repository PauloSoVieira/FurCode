package org.mindera.fur.code.dto.requestDetail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.mindera.fur.code.model.State;
import org.mindera.fur.code.service.AdoptionRequestService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Class containing the data of the RequestDetailDTO.
 */
@Data
@Schema(description = "A request detail")
public class RequestDetailDTO {
    @Schema(description = "The unique identifier of the RequestDetail", example = "1", required = true)
    private Long id;
    @Schema(description = "The id of the person", example = "1", required = true)
    private Long personId;
    @Schema(description = "The state of the RequestDetail", example = "SENT", required = true)
    private State state;
    @Schema(description = "The date of the RequestDetail", example = "2023-01-01", required = true)
    private Date date;
    @Schema(description = "The observation of the RequestDetail", example = "The pet is missing information", required = true)
    private String observation;

    @Autowired
    @Schema(description = "The AdoptionRequestService")
    private AdoptionRequestService adoptionRequestService;

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
    
}
