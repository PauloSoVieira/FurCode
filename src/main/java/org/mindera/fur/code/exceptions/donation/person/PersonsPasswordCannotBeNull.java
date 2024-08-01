package org.mindera.fur.code.exceptions.donation.person;

import org.mindera.fur.code.messages.person.PersonsMessages;

public class PersonsPasswordCannotBeNull extends RuntimeException {
    public PersonsPasswordCannotBeNull() {
        super(PersonsMessages.PASSWORD_CANT_BE_NULL);
    }

    public PersonsPasswordCannotBeNull(String message) {
        super(message);
    }
}
