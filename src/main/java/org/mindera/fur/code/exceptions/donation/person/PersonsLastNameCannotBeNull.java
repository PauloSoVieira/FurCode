package org.mindera.fur.code.exceptions.donation.person;

import org.mindera.fur.code.messages.person.PersonsMessages;

public class PersonsLastNameCannotBeNull extends RuntimeException {
    public PersonsLastNameCannotBeNull() {
        super(PersonsMessages.LAST_NAME_CANT_BE_NULL);
    }

    public PersonsLastNameCannotBeNull(String message) {
        super(message);
    }
}
