package org.mindera.fur.code.messages.pet;

/**
 * Pet messages for exceptions.
 */
public record PetMessages() {
    public static final String PET_NOT_FOUND = "Pet not found with ID: ";
    public static final String PET_TYPE_NOT_FOUND = "Pet type not found with ID: ";
    public static final String SHELTER_NOT_FOUND = "Shelter not found with ID: ";
    public static final String PET_RECORD_NOT_FOUND = "Pet record not found with ID: ";
    public static final String DELETED_PET_NOT_FOUND = "Deleted pet not found with ID: ";
}
