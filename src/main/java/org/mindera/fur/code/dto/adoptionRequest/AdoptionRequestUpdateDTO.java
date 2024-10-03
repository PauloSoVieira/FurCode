package org.mindera.fur.code.dto.adoptionRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mindera.fur.code.dto.requestDetail.RequestDetailUpdateDTO;

import java.util.Set;

@Data
@NoArgsConstructor
@Schema(description = "A request to adopt a pet")
public class AdoptionRequestUpdateDTO {

    @Schema(description = "The id of the adoption request")
    private Long id;

    @Schema(description = "The id of the shelter", example = "1")
    private Long shelterId;

    @Schema(description = "The id of the person", example = "1")
    private Long personId;

    @Schema(description = "The id of the pet", example = "1")
    private Long petId;

    @Schema(description = "The request details of the adoption request")
    private Set<RequestDetailUpdateDTO> requestDetails;
}
