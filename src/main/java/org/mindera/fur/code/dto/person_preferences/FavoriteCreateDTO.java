package org.mindera.fur.code.dto.person_preferences;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class FavoriteCreateDTO {

    @Schema(description = "Favorite ID")
    @Positive(message = "Favorite ID must be greater than 0")
    @Digits(integer = 19, fraction = 0, message = "Person ID must be an integer")
    @NotNull(message = "Favorite ID must be provided")
    private Long personId;

    @Schema(description = "Pet ID")
    @Positive(message = "Pet ID must be greater than 0")
    @Digits(integer = 19, fraction = 0, message = "Pet ID must be an integer")
    @NotNull(message = "Pet ID must be provided")
    private Long petId;
}
