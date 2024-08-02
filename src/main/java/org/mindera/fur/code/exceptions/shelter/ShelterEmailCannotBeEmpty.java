package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterEmailCannotBeEmpty extends RuntimeException {
    public ShelterEmailCannotBeEmpty() {
        super(ShelterMessages.EMAIL_CANT_BE_EMPTY);
    }

    public ShelterEmailCannotBeEmpty(String message) {
        super(message);
    }
}
