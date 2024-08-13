package org.mindera.fur.code.dto.shelterPersonRoles;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "A shelter person roles")
public class ShelterPersonRolesDTO {
    @Schema(description = "The unique identifier of the shelter person roles", example = "1", required = true)
    private Long id;

    @Schema(description = "The unique identifier of the person", example = "1", required = true)
    private Long personId;

    @Schema(description = "The unique identifier of the shelter", example = "1", required = true)
    private Long shelterId;

    @Schema(description = "The role of the person", example = "1", required = true)
    private String role;
}
