package org.mindera.fur.code.exceptions.donation.person;

import org.mindera.fur.code.messages.person.PersonsMessages;

public class PersonsAddressCannotBeEmpty extends RuntimeException {
    public PersonsAddressCannotBeEmpty() {
        super(PersonsMessages.ADDRESS_CANT_BE_EMPTY);
    }

    public PersonsAddressCannotBeEmpty(String message) {
        super(message);
    }
}
