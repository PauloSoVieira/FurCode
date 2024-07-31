package org.mindera.fur.code.exceptions;

public class DonationNotFoundException extends RuntimeException{
    public DonationNotFoundException(String message) {
        super(message);
    }
    public DonationNotFoundException() {
        super("Donation not found");
    }
}
