package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterVatCannotBeEmpty extends RuntimeException {
    public ShelterVatCannotBeEmpty() {
        super(ShelterMessages.VAT_CANT_BE_EMPTY);
    }

    public ShelterVatCannotBeEmpty(String message) {
        super(message);
    }
}
