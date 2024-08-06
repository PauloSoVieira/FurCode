package org.mindera.fur.code.exceptions.adoptionFormException;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Custom exception thrown when an adoption form is not found.
 */
@Schema(description = "Exception thrown when an adoption form is not found.")
public class AdoptionFormNotFound extends RuntimeException {

    /**
     * Constructs a new AdoptionFormNotFound exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public AdoptionFormNotFound(String message) {
        super(message);
    }

}
