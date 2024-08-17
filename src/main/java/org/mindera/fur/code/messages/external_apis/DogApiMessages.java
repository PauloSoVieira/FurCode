package org.mindera.fur.code.messages.external_apis;

/**
 * Dog API messages for exceptions.
 */
public record DogApiMessages() {
    public static final String DOG_API_BREED_NOT_FOUND_WITH_NAME = "Breed not found with name: ";
    public static final String DOG_API_BREED_NOT_FOUND_WITH_ID = "Breed not found with ID: ";
    public static final String DOG_API_BREED_NOT_FOUND_WITH_URL = "Breed not found with url: ";
    public static final String DOG_API_BREEDS_FAILURE = "Failed to fetch breed from the external DOG API";
    public static final String DOG_API_BREEDS_NAMES_FAILURE = "Failed to fetch breeds names from the external API";
    public static final String DOG_API_COMMUNICATION_FAILURE = "An error occurred while communicating with the external API";
}
