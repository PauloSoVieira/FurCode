package org.mindera.fur.code.exceptions.requestDetail;

/**
 * Class representing an exception when a RequestDetail is not found.
 */
public class RequestDetailNotFound extends RuntimeException {
    public RequestDetailNotFound(String message) {
        super(message);
    }
}
