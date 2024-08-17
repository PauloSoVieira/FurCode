package org.mindera.fur.code.dto.adoptionRequest;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.mindera.fur.code.dto.requestDetail.RequestDetailDTO;

import java.util.HashSet;
import java.util.Set;

/**
 * DTO for creating an adoption request.
 */
@Data
@Schema(description = "An adoption request creation request")
public class AdoptionRequestCreationDTO {

    @NotNull
    @Schema(description = "The id of the shelter", example = "1", required = true)
    private Long shelterId;

    @NotNull
    @Schema(description = "The id of the person", example = "1", required = true)
    private Long personId;

    @NotNull
    @Schema(description = "The id of the pet", example = "1", required = true)
    private Long petId;

    @NotNull
    @NotEmpty
    private Set<RequestDetailDTO> requestDetails;

    /**
     * Default constructor.
     */
    public AdoptionRequestCreationDTO() {
    }

    /**
     * Constructor with parameters.
     *
     * @param shelterId The id of the shelter.
     * @param personId  The id of the person.
     * @param petId     The id of the pet.
     */
    public AdoptionRequestCreationDTO(Long shelterId, Long personId, Long petId, Set<RequestDetailDTO> requestDetails) {
        this.shelterId = shelterId;
        this.personId = personId;
        this.petId = petId;
        this.requestDetails = requestDetails == null ? new HashSet<>() : requestDetails;
    }

    //TODO VERIFY HOW TO LEAVE SOMETHING TO SENT BY DEFAULT
}
