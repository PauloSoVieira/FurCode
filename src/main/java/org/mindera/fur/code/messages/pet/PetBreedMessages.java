package org.mindera.fur.code.messages.pet;

/**
 * Messages for Pet Breed
 */
public record PetBreedMessages() {
    public static final String PET_BREED_NOT_FOUND_WITH_NAME = "Breed not found in external API with name: ";
    public static final String PET_TYPE_NOT_SUPPORTED = "Currently, only Dog breeds are supported. Provided species: ";
}
