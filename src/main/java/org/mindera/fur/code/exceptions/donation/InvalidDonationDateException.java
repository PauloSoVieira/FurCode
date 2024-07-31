package org.mindera.fur.code.exceptions.donation;

public class InvalidDonationDateException extends RuntimeException {
    public InvalidDonationDateException(String message) {
        super(message);
    }

    public InvalidDonationDateException() {
        super("Donation date invalid");
    }
}
