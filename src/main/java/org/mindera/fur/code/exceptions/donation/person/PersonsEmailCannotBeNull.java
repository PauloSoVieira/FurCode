package org.mindera.fur.code.exceptions.donation.person;

import org.mindera.fur.code.messages.person.PersonsMessages;

public class PersonsEmailCannotBeNull extends RuntimeException {
    public PersonsEmailCannotBeNull() {
        super(PersonsMessages.EMAIL_CANT_BE_NULL);
    }

    public PersonsEmailCannotBeNull(String message) {
        super(message);
    }
}
