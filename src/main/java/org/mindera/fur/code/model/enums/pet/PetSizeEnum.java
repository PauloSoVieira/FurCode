package org.mindera.fur.code.model.enums.pet;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Pet size enum
 */
@Getter
@AllArgsConstructor
public enum PetSizeEnum {
    SMALL(1, "Small"),
    MEDIUM(2, "Medium"),
    LARGE(3, "Large");

    private final int id;
    private final String name;
}
