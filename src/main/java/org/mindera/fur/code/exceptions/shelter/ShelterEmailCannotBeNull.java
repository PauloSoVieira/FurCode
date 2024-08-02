package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterEmailCannotBeNull extends RuntimeException {
    public ShelterEmailCannotBeNull() {
        super(ShelterMessages.EMAIL_CANT_BE_NULL);
    }

    public ShelterEmailCannotBeNull(String message) {
        super(message);
    }
}
