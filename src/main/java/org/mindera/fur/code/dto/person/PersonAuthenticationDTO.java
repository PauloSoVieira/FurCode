package org.mindera.fur.code.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "A person authentication information")
@Data

/**
 * A person authentication information
 */
public class PersonAuthenticationDTO {
    @Schema(description = "The email address of the person", example = "john.doe@example.com", required = true)
    private String email;
    @Schema(description = "The password of the person", example = "password", required = true)
    private String password;

}
