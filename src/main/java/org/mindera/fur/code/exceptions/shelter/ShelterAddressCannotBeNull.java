package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterAddressCannotBeNull extends RuntimeException {
    public ShelterAddressCannotBeNull() {
        super(ShelterMessages.ADDRESS_CANT_BE_NULL);
    }

    public ShelterAddressCannotBeNull(String message) {
        super(message);
    }
}
