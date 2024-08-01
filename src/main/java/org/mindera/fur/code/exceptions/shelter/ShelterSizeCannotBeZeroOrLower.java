package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterSizeCannotBeZeroOrLower extends RuntimeException {
    public ShelterSizeCannotBeZeroOrLower() {
        super(ShelterMessages.SIZE_CANT_BE_ZERO_OR_LOWER);
    }

    public ShelterSizeCannotBeZeroOrLower(String message) {
        super(message);
    }
}
