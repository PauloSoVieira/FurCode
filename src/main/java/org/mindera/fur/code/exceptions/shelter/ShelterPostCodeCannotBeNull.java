package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterPostCodeCannotBeNull extends RuntimeException {
    public ShelterPostCodeCannotBeNull() {
        super(ShelterMessages.POST_CODE_CANT_BE_NULL);
    }

    public ShelterPostCodeCannotBeNull(String message) {
        super(message);
    }
}
