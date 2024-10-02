package org.mindera.fur.code.dto.adoptionRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mindera.fur.code.dto.requestDetail.RequestDetailDTO;

import java.util.Set;

/**
 * DTO for an adoption request.
 */
@Data
@NoArgsConstructor
@Schema(description = "DTO for an adoption request")
public class AdoptionRequestDTO {

    @Positive(message = "Adoption request ID must be greater than 0")
    @NotNull(message = "Adoption request ID must be provided")
    @Schema(description = "The unique identifier of the adoption request", example = "1")
    private Long id;

    @Positive(message = "Shelter ID must be greater than 0")
    @NotNull(message = "Shelter ID must be provided")
    @Schema(description = "The id of the shelter", example = "1")
    private Long shelterId;

    @Positive(message = "Person ID must be greater than 0")
    @NotNull(message = "Person ID must be provided")
    @Schema(description = "The id of the person", example = "1")
    private Long personId;

    @Positive(message = "Pet ID must be greater than 0")
    @NotNull(message = "Pet ID must be provided")
    @Schema(description = "The id of the pet", example = "1")
    private Long petId;

    @NotNull(message = "Request details must be provided")
    @Schema(description = "The request details of the adoption request")
    private Set<RequestDetailDTO> requestDetails;
}
