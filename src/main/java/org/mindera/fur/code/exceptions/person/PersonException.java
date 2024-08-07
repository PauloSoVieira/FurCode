package org.mindera.fur.code.exceptions.person;

public class PersonException extends RuntimeException {

    /**
     * Constructs a new PersonException with the specified message.
     */
    public PersonException() {
    }


    /**
     * Constructs a new PersonException with the specified message and cause.
     */
    public PersonException(String message) {
        super(message);
    }
}

