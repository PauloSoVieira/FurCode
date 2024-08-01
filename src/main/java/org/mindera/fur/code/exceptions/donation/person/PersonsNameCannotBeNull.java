package org.mindera.fur.code.exceptions.donation.person;

import org.mindera.fur.code.messages.person.PersonsMessages;

public class PersonsNameCannotBeNull extends RuntimeException {
    public PersonsNameCannotBeNull() {
        super(PersonsMessages.NAME_CANT_BE_NULL);
    }

    public PersonsNameCannotBeNull(String message) {
        super(message);
    }
}
