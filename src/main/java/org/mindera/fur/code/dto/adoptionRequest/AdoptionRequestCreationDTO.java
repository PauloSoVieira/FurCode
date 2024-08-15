package org.mindera.fur.code.dto.adoptionRequest;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for creating an adoption request.
 */
@Data
@Schema(description = "An adoption request creation request")
public class AdoptionRequestCreationDTO {

    @NotNull
    @NotBlank
    @Schema(description = "The id of the shelter", example = "1", required = true)
    private Long shelterId;

    @NotNull
    @NotBlank
    @Schema(description = "The id of the person", example = "1", required = true)
    private Long personId;

    @NotNull
    @NotBlank
    @Schema(description = "The id of the pet", example = "1", required = true)
    private Long petId;

    @NotNull
    @NotBlank
    private Set<RequestDetailDTO> requestDetails;

    /**
     * Default constructor.
     */
    public AdoptionRequestCreationDTO() {
    }

    /**
     * Constructor with parameters.
     *
     * @param shelterId      The id of the shelter.
     * @param personId       The id of the person.
     * @param petId          The id of the pet.
     * @param requestDetails The request details.
     */
    public AdoptionRequestCreationDTO(Long shelterId, Long personId, Long petId) {
        this.shelterId = shelterId;
        this.personId = personId;
        this.petId = petId;
        this.requestDetails = requestDetails;
    }

    //TODO VERIFY HOW TO LEAVE SOMETHING TO SENT BY DEFAULT
}
