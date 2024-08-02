package org.mindera.fur.code.exceptions.donation.person;

import org.mindera.fur.code.messages.person.PersonsMessages;

public class PersonsIdCannotBeLowerOrEqualZero extends RuntimeException {
    public PersonsIdCannotBeLowerOrEqualZero(String message) {
        super(message);
    }

    public PersonsIdCannotBeLowerOrEqualZero() {
        super(PersonsMessages.ID_CANT_BE_LOWER_OR_EQUAL_ZERO);
    }
}
