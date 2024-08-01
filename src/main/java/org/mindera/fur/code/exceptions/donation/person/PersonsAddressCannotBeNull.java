package org.mindera.fur.code.exceptions.donation.person;

import org.mindera.fur.code.messages.person.PersonsMessages;

public class PersonsAddressCannotBeNull extends RuntimeException {
    public PersonsAddressCannotBeNull() {
        super(PersonsMessages.ADDRESS_CANT_BE_NULL);
    }

    public PersonsAddressCannotBeNull(String message) {
        super(message);
    }
}
