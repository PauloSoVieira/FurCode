package org.mindera.fur.code.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The state of a pet", example = "SENT")
public enum State {

    @Schema(description = "The state of a pet as sent", example = "SENT")
    SENT,

    @Schema(description = "The state of a pet as waiting for verification", example = "WAITING_FOR_VERIFICATION")
    VERIFYING_INFORMATION,

    @Schema(description = "The state of a pet as missing information", example = "MISSING_INFORMATION")
    MISSING_INFORMATION,

    @Schema(description = "The state of a pet as accepted", example = "ACCEPTED")
    ACCEPTED,

    @Schema(description = "The state of a pet as refused", example = "REFUSED")
    REFUSED,

    @Schema(description = "The state of a pet as cancelled", example = "CANCELLED")
    CANCELLED;

    private String State;

    State() {
    }

    public String getState() {
        return State;
    }
}
