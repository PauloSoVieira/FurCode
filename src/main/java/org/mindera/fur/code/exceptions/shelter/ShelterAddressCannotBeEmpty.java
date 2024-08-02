package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterAddressCannotBeEmpty extends RuntimeException {
    public ShelterAddressCannotBeEmpty() {
        super(ShelterMessages.ADDRESS_CANT_BE_EMPTY);
    }

    public ShelterAddressCannotBeEmpty(String message) {
        super(message);
    }
}
