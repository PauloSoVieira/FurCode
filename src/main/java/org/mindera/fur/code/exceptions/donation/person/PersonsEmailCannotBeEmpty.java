package org.mindera.fur.code.exceptions.donation.person;

import org.mindera.fur.code.messages.person.PersonsMessages;

public class PersonsEmailCannotBeEmpty extends RuntimeException {
    public PersonsEmailCannotBeEmpty() {
        super(PersonsMessages.EMAIL_CANT_BE_EMPTY);
    }

    public PersonsEmailCannotBeEmpty(String message) {
        super(message);
    }
}
