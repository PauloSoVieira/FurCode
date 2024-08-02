package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterSizeCannotBeEmpty extends RuntimeException {
    public ShelterSizeCannotBeEmpty() {
        super(ShelterMessages.SIZE_CANT_BE_EMPTY);
    }

    public ShelterSizeCannotBeEmpty(String message) {
        super(message);
    }
}
