package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterPostCodeCannotBeEmpty extends RuntimeException {
    public ShelterPostCodeCannotBeEmpty() {
        super(ShelterMessages.POST_CODE_CANT_BE_EMPTY);
    }

    public ShelterPostCodeCannotBeEmpty(String message) {
        super(message);
    }
}
