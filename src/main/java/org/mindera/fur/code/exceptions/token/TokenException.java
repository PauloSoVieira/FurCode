package org.mindera.fur.code.exceptions.token;

public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super("Invalid token: " + message);
    }
}
