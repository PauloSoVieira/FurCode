package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterVatCannotBeNull extends RuntimeException {
    public ShelterVatCannotBeNull() {
        super(ShelterMessages.VAT_CANT_BE_NULL);
    }

    public ShelterVatCannotBeNull(String message) {
        super(message);
    }
}
