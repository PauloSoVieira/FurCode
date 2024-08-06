package org.mindera.fur.code.dto.shelterPersonRoles;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.mindera.fur.code.model.Role;

@Data
public class ShelterPersonRolesCreationDTO {

    @NotNull
    @NotBlank
    private Long personId;

    @NotNull
    @NotBlank
    private Long shelterId;

    @NotNull
    @NotBlank
    private Role role;


}
