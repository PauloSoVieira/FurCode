package org.mindera.fur.code.exceptions.donation.person;

import org.mindera.fur.code.messages.person.PersonsMessages;

public class PersonsLastNameCannotBeEmpty extends RuntimeException {
    public PersonsLastNameCannotBeEmpty() {
        super(PersonsMessages.LAST_NAME_CANT_BE_EMPTY);
    }

    public PersonsLastNameCannotBeEmpty(String message) {
        super(message);
    }
}
