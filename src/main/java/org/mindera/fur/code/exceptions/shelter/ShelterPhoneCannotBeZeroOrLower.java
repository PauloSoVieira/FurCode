package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterPhoneCannotBeZeroOrLower extends RuntimeException {
    public ShelterPhoneCannotBeZeroOrLower() {
        super(ShelterMessages.PHONE_CANT_BE_ZERO_OR_LOWER);
    }

    public ShelterPhoneCannotBeZeroOrLower(String message) {
        super(message);
    }
}
