package org.mindera.fur.code.exceptions.donation.person;

import org.mindera.fur.code.messages.person.PersonsMessages;

public class PersonsNameCannotBeEmpty extends RuntimeException {
    public PersonsNameCannotBeEmpty() {
        super(PersonsMessages.NAME_CANT_BE_EMPTY);
    }

    public PersonsNameCannotBeEmpty(String message) {
        super(message);
    }
}
