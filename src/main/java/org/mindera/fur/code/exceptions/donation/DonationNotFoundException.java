package org.mindera.fur.code.exceptions.donation;

public class DonationNotFoundException extends RuntimeException {
    public DonationNotFoundException(String message) {
        super(message);
    }

    public DonationNotFoundException() {
        super("Donation not found");
    }
}
