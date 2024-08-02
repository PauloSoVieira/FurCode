package org.mindera.fur.code.exceptions.shelter;

import org.mindera.fur.code.messages.shelter.ShelterMessages;

public class ShelterVatCannotBeZeroOrLower extends RuntimeException {
    public ShelterVatCannotBeZeroOrLower() {
        super(ShelterMessages.VAT_CANT_BE_ZERO_OR_LOWER);
    }

    public ShelterVatCannotBeZeroOrLower(String message) {
        super(message);
    }
}
