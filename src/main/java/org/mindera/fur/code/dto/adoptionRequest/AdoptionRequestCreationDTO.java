package org.mindera.fur.code.dto.adoptionRequest;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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

    public AdoptionRequestCreationDTO() {
    }

    public AdoptionRequestCreationDTO(Long shelterId, Long personId, Long petId) {
        this.shelterId = shelterId;
        this.personId = personId;
        this.petId = petId;
        //this.requestDetails = requestDetails;
    }

    //TODO VERIFY HOW TO LEAVE SOMETHING TO SENT BY DEFAULT
}
