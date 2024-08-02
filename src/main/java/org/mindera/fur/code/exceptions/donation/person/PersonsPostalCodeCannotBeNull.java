package org.mindera.fur.code.exceptions.donation.person;

import org.mindera.fur.code.messages.person.PersonsMessages;

public class PersonsPostalCodeCannotBeNull extends RuntimeException {
    public PersonsPostalCodeCannotBeNull() {
        super(PersonsMessages.POSTAL_CODE_CANT_BE_NULL);
    }

    public PersonsPostalCodeCannotBeNull(String message) {
        super(message);
    }
}
