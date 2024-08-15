package org.mindera.fur.code.exceptions.requestDetail;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Class representing an exception when a RequestDetail is not found.
 */
@Schema(description = "Exception thrown when a RequestDetail is not found.")
public class RequestDetailNotFound extends RuntimeException {
    public RequestDetailNotFound(String message) {
        super(message);
    }
}
