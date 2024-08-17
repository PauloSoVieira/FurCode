package org.mindera.fur.code.exceptions.external_apis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Dog API Exception class for handling external API-related exceptions.
 */
@Getter
@AllArgsConstructor
public class DogApiException extends RuntimeException {
    private final String message;
    private final HttpStatus status;
}
