package org.mindera.fur.code.model;

public class ConfirmDonationResponse {
    private String message;

    /**
     * Constructor
     * @param message
     */
    public ConfirmDonationResponse(String message) {
        this.message = message;
    }

    /**
     * Get the message
     * @return
     */
    public String getMessage() {
        return message;
    }


}
