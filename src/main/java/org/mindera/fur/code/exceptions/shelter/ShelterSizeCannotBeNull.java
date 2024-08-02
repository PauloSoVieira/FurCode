package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterSizeCannotBeNull extends RuntimeException {
    public ShelterSizeCannotBeNull() {
        super(ShelterMessages.SIZE_CANT_BE_NULL);
    }

    public ShelterSizeCannotBeNull(String message) {
        super(message);
    }
}
