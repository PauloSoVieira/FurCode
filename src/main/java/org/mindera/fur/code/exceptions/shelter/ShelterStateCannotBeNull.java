package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterStateCannotBeNull extends RuntimeException {
    public ShelterStateCannotBeNull() {
        super(ShelterMessages.ISACTIVE_CANT_BE_NULL);
    }

    public ShelterStateCannotBeNull(String message) {
        super(message);
    }
}
