package org.mindera.fur.code.dto.shelter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShelterThemeDTO {
    private Long id;

    @NotNull
    @NotBlank
    @Valid
    private String name;

    @NotNull
    @NotBlank
    @Valid
    private String accentColor;

    @NotNull
    @NotBlank
    @Valid
    private String backgroundColor;

    @NotNull
    @NotBlank
    @Valid
    private String titleFontName;
}
