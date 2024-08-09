package org.mindera.fur.code.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A login response")
public record LoginResponseDTO(String token) {
}
