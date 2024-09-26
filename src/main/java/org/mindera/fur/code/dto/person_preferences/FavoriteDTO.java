package org.mindera.fur.code.dto.person_preferences;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FavoriteDTO {

    @Schema(description = "Favorite ID")
    @Positive(message = "Favorite ID must be greater than 0")
    @NotNull(message = "Favorite ID must be provided")
    private Long personId;

    @Schema(description = "Pet ID")
    @Positive(message = "Pet ID must be greater than 0")
    @NotNull(message = "Pet ID must be provided")
    private Long petId;

    @Schema(description = "The date and time the pet was favorited")
    private LocalDateTime favoritedAt;
}
