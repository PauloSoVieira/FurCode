package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterNameCannotBeNull extends RuntimeException {
    public ShelterNameCannotBeNull() {
        super(ShelterMessages.NAME_CANT_BE_NULL);
    }

    public ShelterNameCannotBeNull(String message) {
        super(message);
    }
}
