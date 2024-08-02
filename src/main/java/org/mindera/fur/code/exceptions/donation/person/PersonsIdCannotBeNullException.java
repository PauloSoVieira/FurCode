package org.mindera.fur.code.exceptions.donation.person;

import org.mindera.fur.code.messages.person.PersonsMessages;

public class PersonsIdCannotBeNullException extends RuntimeException {

    public PersonsIdCannotBeNullException(String message) {
        super(message);
    }

    public PersonsIdCannotBeNullException() {
        super(PersonsMessages.ID_CANT_BE_NULL);
    }
}
