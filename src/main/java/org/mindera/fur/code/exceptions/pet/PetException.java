package org.mindera.fur.code.exceptions.pet;

/**
 * Pet Exception class for handling pet-related exceptions.
 */
public class PetException extends RuntimeException {

    /**
     * Constructs a new PetException with the specified message.
     */
    public PetException() {
    }

    /**
     * Constructs a new PetException with the specified message and cause.
     */
    public PetException(String message) {
        super(message);
    }
}
