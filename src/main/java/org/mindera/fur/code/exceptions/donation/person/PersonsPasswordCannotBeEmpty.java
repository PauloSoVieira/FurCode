package org.mindera.fur.code.exceptions.donation.person;

import org.mindera.fur.code.messages.person.PersonsMessages;

public class PersonsPasswordCannotBeEmpty extends RuntimeException {
    public PersonsPasswordCannotBeEmpty() {
        super(PersonsMessages.PASSWORD_CANT_BE_EMPTY);
    }

    public PersonsPasswordCannotBeEmpty(String message) {
        super(message);
    }
}
