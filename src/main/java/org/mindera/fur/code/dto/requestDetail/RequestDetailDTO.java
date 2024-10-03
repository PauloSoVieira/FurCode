package org.mindera.fur.code.dto.requestDetail;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mindera.fur.code.model.State;

import java.time.LocalDate;

/**
 * Class containing the data of the RequestDetailDTO.
 */
@Data
@NoArgsConstructor
@Schema(description = "A request detail")
public class RequestDetailDTO {

    @Positive(message = "ID must be greater than 0")
    @NotNull(message = "ID must be provided")
    @Schema(description = "The unique identifier of the RequestDetail", example = "1")
    private Long id;

    @Positive(message = "Person ID must be greater than 0")
    @NotNull(message = "Person ID must be provided")
    @Schema(description = "The id of the person", example = "1")
    private Long personId;

    @NotNull(message = "State must be provided")
    @Schema(description = "The state of the RequestDetail", example = "SENT")
    private State state;

    @NotNull(message = "Date must be provided")
    @Schema(description = "The date of the RequestDetail", example = "2023-01-01")
    private LocalDate date;

    @NotBlank(message = "Observation must be provided")
    @Size(max = 1000, message = "Observation must be less than 1000 characters")
    @Schema(description = "The observation of the RequestDetail", example = "The pet is missing information")
    private String observation;
}
