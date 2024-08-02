package org.mindera.fur.code.model;

public enum State {
    SENT,
    VERIFYING_INFORMATION,
    MISSING_INFORMATION,
    ACCEPTED,
    REFUSED;

    private String State;

    State() {
    }

    public String getState() {
        return State;
    }
}
