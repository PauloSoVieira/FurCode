package org.mindera.fur.code.exceptions.donation.person;

import org.mindera.fur.code.messages.person.PersonsMessages;

public class PersonsPostalCodeCannotBeZero extends RuntimeException {
    public PersonsPostalCodeCannotBeZero() {
        super(PersonsMessages.POSTAL_CODE_CANT_BE_ZERO);
    }

    public PersonsPostalCodeCannotBeZero(String message) {
        super(message);
    }
}
