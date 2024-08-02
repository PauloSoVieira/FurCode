package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterNameCannotBeEmpty extends RuntimeException {
    public ShelterNameCannotBeEmpty() {
        super(ShelterMessages.NAME_CANT_BE_EMPTY);
    }

    public ShelterNameCannotBeEmpty(String message) {
        super(message);
    }
}
