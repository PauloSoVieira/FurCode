package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterPhoneCannotBeNull extends RuntimeException {
    public ShelterPhoneCannotBeNull() {
        super(ShelterMessages.PHONE_CANT_BE_NULL);
    }

    public ShelterPhoneCannotBeNull(String message) {
        super(message);
    }
}
