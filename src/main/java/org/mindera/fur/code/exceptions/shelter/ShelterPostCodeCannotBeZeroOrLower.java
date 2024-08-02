package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterPostCodeCannotBeZeroOrLower extends RuntimeException {
    public ShelterPostCodeCannotBeZeroOrLower() {
        super(ShelterMessages.POST_CODE_CANT_BE_ZERO_OR_LOWER);
    }

    public ShelterPostCodeCannotBeZeroOrLower(String message) {
        super(message);
    }
}
