package org.mindera.fur.code.exceptions.donation;

public class InvalidDonationAmountException extends RuntimeException {
    public InvalidDonationAmountException(String message) {
        super(message);
    }

    public InvalidDonationAmountException() {
        super("Donation amount must be greater than 0");
    }
}
