package org.mindera.fur.code.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The state of a pet", example = "SENT", required = true)
public enum State {

    @Schema(description = "The state of a pet as sent", example = "SENT", required = true)
    SENT,

    @Schema(description = "The state of a pet as waiting for verification", example = "WAITING_FOR_VERIFICATION", required = true)
    VERIFYING_INFORMATION,

    @Schema(description = "The state of a pet as missing information", example = "MISSING_INFORMATION", required = true)
    MISSING_INFORMATION,

    @Schema(description = "The state of a pet as accepted", example = "ACCEPTED", required = true)
    ACCEPTED,

    @Schema(description = "The state of a pet as refused", example = "REFUSED", required = true)
    REFUSED;

    private String State;

    State() {
    }

    public String getState() {
        return State;
    }
}
