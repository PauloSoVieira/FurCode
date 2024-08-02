package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterPhoneCannotBeEmpty extends RuntimeException {
    public ShelterPhoneCannotBeEmpty() {
        super(ShelterMessages.PHONE_CANT_BE_EMPTY);
    }

    public ShelterPhoneCannotBeEmpty(String message) {
        super(message);
    }
}
