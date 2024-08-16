package org.mindera.fur.code.messages.shelter;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Class containing the messages for the ShelterMessage.
 */
@Schema(description = "The shelter messages")
public class ShelterMessages {
    /**
     * Message for when the name is null.
     */
    public static final String NAME_CANT_BE_EMPTY = "Name can't be empty";
    /**
     * Message for when the name is null.
     */
    public static final String NAME_CANT_BE_NULL = "Name can't be null";
    /**
     * Message for when the vat is null.
     */
    public static final String VAT_CANT_BE_EMPTY = "VAT can't be empty";
    /**
     * Message for when the vat is zero or lower.
     */
    public static final String VAT_CANT_BE_ZERO_OR_LOWER = "VAT can't be zero or lower";
    /**
     * Message for when the vat is null.
     */
    public static final String VAT_CANT_BE_NULL = "VAT can't be null";
    /**
     * Message for when the email is null.
     */
    public static final String EMAIL_CANT_BE_EMPTY = "Email can't be empty";
    /**
     * Message for when the email is null.
     */
    public static final String EMAIL_CANT_BE_NULL = "Email can't be null";
    /**
     * Message for when the address is null.
     */
    public static final String ADDRESS_CANT_BE_EMPTY = "Address can't be empty";
    /**
     * Message for when the address is null.
     */
    public static final String ADDRESS_CANT_BE_NULL = "Address can't be null";
    /**
     * Message for when the phone is null.
     */
    public static final String PHONE_CANT_BE_EMPTY = "Phone Number can't be empty";
    /**
     * Message for when the phone is zero or lower.
     */
    public static final String PHONE_CANT_BE_ZERO_OR_LOWER = "Phone Number can't be zero or Lower";
    /**
     * Message for when the phone is null.
     */
    public static final String PHONE_CANT_BE_NULL = "Phone Number can't be null";
    /**
     * Message for when the size is null.
     */
    public static final String SIZE_CANT_BE_EMPTY = "Size can't be empty";
    /**
     * Message for when the size is zero or lower.
     */
    public static final String SIZE_CANT_BE_ZERO_OR_LOWER = "Size can't be zero or lower";
    /**
     * Message for when the size is null.
     */
    public static final String SIZE_CANT_BE_NULL = "Size can't be null";
    /**
     * Message for when the size is higher than 1000.
     */
    public static final String SIZE_CANT_BE_HIGHER_THAN_1000 = "Size can't be higher than 1000";
    /**
     * Message for when the isActive is null.
     */
    public static final String ISACTIVE_CANT_BE_NULL = "State can't be null";
    /**
     * Message for when the shelter is not found.
     */
    public static final String SHELTER_NOT_FOUND = "Shelter not found";
    /**
     * Message for when the id is null.
     */
    public static final String ID_CANT_BE_NULL = "Id can't be null";
    /**
     * Message for when the id is lower or equal zero.
     */
    public static final String ID_CANT_BE_LOWER_OR_EQUAL_ZERO = "Id can't be lower or equal zero";
    /**
     * Message for when the postal code is null.
     */
    public static final String POSTAL_CODE_CANT_BE_NULL = "Postal Code can't be null";
    /**
     * Message for when the postal code is empty.
     */
    public static final String POSTAL_CODE_CANT_BE_EMPTY = "Postal Code can't be empty";
    /**
     * Message for when the creation date is null.
     */
    public static final String CREATION_DATE_CANT_BE_NULL = "Creation Date can't be null";
    /**
     * Message for when the creation date is empty.
     */
    public static final String CREATION_DATE_CANT_BE_EMPTY = "Creation Date can't be empty";
    /**
     * Message for when the creation date is in future.
     */
    public static final String CREATION_DATE_CANT_BE_IN_FUTURE = "Creation Date can't be in future";
    /**
     * Message for when the email format is invalid.
     */
    public static final String EMAIL_FORMAT_INVALID = "Email format is invalid";
}
