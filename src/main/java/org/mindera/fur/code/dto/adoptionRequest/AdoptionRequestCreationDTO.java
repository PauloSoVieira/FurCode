package org.mindera.fur.code.dto.adoptionRequest;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.mindera.fur.code.model.State;

import java.util.Date;

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

    @NotNull
    @NotBlank
    private State state;

    @NotNull
    @NotBlank
    private Date date;

    public AdoptionRequestCreationDTO() {
    }

    public AdoptionRequestCreationDTO(Long shelterId, Long personId, Long petId, State state, Date date) {
        this.shelterId = shelterId;
        this.personId = personId;
        this.petId = petId;
        this.state = state;
        this.date = date;
    }

    //TODO VERIFY HOW TO LEAVE SOMETHING TO SENT BY DEFAULT
}
