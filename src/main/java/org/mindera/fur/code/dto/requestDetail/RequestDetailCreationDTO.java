package org.mindera.fur.code.dto.requestDetail;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.mindera.fur.code.model.State;

import java.time.LocalDate;

/**
 * Class containing the data of the RequestDetailCreationDTO.
 */
@Data
@Schema(description = "A request detail creation request")
public class RequestDetailCreationDTO {

    @NotNull
    @NotBlank
    @Schema(description = "The id of the person", example = "1", required = true)
    private Long personId;

    @NotNull
    @NotBlank
    @Schema(description = "The state of the RequestDetail", example = "SENT", required = true)
    private State state;

    @NotNull
    @NotBlank
    @Schema(description = "The date of the RequestDetail", example = "2023-01-01", required = true)
    private LocalDate date;

    @Schema(description = "The observation of the RequestDetail", example = "The pet is missing information", required = true)
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
    public RequestDetailCreationDTO(Long personId, State state, LocalDate date, String observation) {
        this.personId = personId;
        this.state = state;
        this.date = date;
        this.observation = observation;
    }
}
