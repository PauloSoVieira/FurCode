package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterStateCannotBeEmpty extends RuntimeException {
    public ShelterStateCannotBeEmpty() {
        super(ShelterMessages.ISACTIVE_CANT_BE_EMPTY);
    }

    public ShelterStateCannotBeEmpty(String message) {
        super(message);
    }
}
