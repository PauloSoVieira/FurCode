package org.mindera.fur.code.messages.pet;

/**
 * Pet messages for exceptions.
 */
public record PetMessages() {

    public static final String PET_NOT_FOUND = "Pet not found with ID: ";
    public static final String PET_TYPE_NOT_FOUND = "Pet type not found with ID: ";
    public static final String SHELTER_NOT_FOUND = "Shelter not found with ID: ";
    public static final String PET_RECORD_NOT_FOUND = "Pet record not found with pet ID: ";
    public static final String BREED_NOT_FOUND_WITH_ID = "Breed not found with ID: ";
    public static final String BREED_NOT_FOUND_WITH_NAME = "Breed not found with name: ";
    public static final String BREED_NOT_FOUND_EXTERNAL_API_WITH_NAME = "Breed not found in external API with name: ";
    public static final String TYPE_OF_BREED_NOT_SUPPORTED = "Currently, only Dog breeds are supported. Provided species: ";
    public static final String EXTERNAL_DOG_API_BREEDS_FAILURE = "Failed to fetch breed from the external DOG API";
    public static final String EXTERNAL_DOG_API_BREEDS_NAMES_FAILURE = "Failed to fetch breeds names from the external API";
    public static final String EXTERNAL_DOG_API_COMMUNICATION_FAILURE = "An error occurred while communicating with the external API";
}
