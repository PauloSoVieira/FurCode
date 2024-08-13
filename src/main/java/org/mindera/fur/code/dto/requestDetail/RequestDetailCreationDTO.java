package org.mindera.fur.code.dto.requestDetail;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.mindera.fur.code.model.State;

import java.util.Date;

/**
 * Class containing the data of the RequestDetailCreationDTO.
 */
@Data
public class RequestDetailCreationDTO {

    @NotNull
    @NotBlank
    private Long personId;

    @NotNull
    @NotBlank
    private State state;

    @NotNull
    @NotBlank
    private Date date;

    private String observation;

    /**
     * Constructor default.
     */
    public RequestDetailCreationDTO() {
    }

    /**
     * Constructor with parameters.
     *
     * @param personId    The id of the person.
     * @param state       The state of the RequestDetail.
     * @param date        The date of the RequestDetail.
     * @param observation The observation of the RequestDetail.
     */
    public RequestDetailCreationDTO(Long personId, State state, Date date, String observation) {
        this.personId = personId;
        this.state = state;
        this.date = date;
        this.observation = observation;
    }
}
