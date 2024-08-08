package org.mindera.fur.code.dto.shelterPersonRoles;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.mindera.fur.code.model.Role;

@Data
@Schema(description = "A shelter person roles creation request")
public class ShelterPersonRolesCreationDTO {

    @NotNull
    @NotBlank
    @Schema(description = "The id of the person", example = "1", required = true)
    private Long personId;

    @NotNull
    @NotBlank
    @Schema(description = "The id of the shelter", example = "1", required = true)
    private Long shelterId;

    @NotNull
    @NotBlank
    @Schema(description = "The role of the person", example = "1", required = true)
    private Role role;


}
