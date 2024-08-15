package org.mindera.fur.code.exceptions.shelter;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Exception class for when a shelter is not found.
 */
@Schema(description = "Exception thrown when a shelter is not found.")
public class ShelterNotFound extends RuntimeException {

    /**
     * Constructor with message.
     *
     * @param message The message for the exception.
     */
    public ShelterNotFound(String message) {
        super(message);
    }

}
