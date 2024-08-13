package org.mindera.fur.code.dto.adoptionRequest;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for creating an adoption request.
 */
@Data
public class AdoptionRequestCreationDTO {

    @NotNull
    @NotBlank
    private Long shelterId;

    @NotNull
    @NotBlank
    private Long personId;

    @NotNull
    @NotBlank
    private Long petId;

    //@NotNull
    //@NotBlank
    //private Set<RequestDetailDTO> requestDetails;

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
    public AdoptionRequestCreationDTO(Long shelterId, Long personId, Long petId) {
        this.shelterId = shelterId;
        this.personId = personId;
        this.petId = petId;
        //this.requestDetails = requestDetails;
    }

    //TODO VERIFY HOW TO LEAVE SOMETHING TO SENT BY DEFAULT
}
