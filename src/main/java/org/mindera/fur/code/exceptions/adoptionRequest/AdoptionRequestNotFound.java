package org.mindera.fur.code.exceptions.adoptionRequest;

/**
 * Exception class for when an adoption request is not found.
 */
public class AdoptionRequestNotFound extends RuntimeException {

    /**
     * Constructor with message.
     *
     * @param message The message for the exception.
     */
    public AdoptionRequestNotFound(String message) {
        super(message);
    }

}