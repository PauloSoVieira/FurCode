package org.mindera.fur.code.dto.requestDetail;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mindera.fur.code.model.State;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(description = "RequestDetail update DTO")
public class RequestDetailUpdateDTO {

    @Schema(description = "The id of the request detail")
    private Long id;

    @Schema(description = "The id of the person", example = "1")
    private Long personId;

    @Schema(description = "The state of the RequestDetail", example = "SENT")
    private State state;

    @Schema(description = "The date of the RequestDetail", example = "2023-01-01")
    private LocalDate date;

    @Size(max = 1000, message = "Observation must be less than 1000 characters")
    @Schema(description = "The observation of the RequestDetail", example = "The pet is missing information")
    private String observation;
}
