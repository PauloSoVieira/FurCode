package org.mindera.fur.code.model.enums.pet;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Pet species enum
 */
@Getter
@AllArgsConstructor
public enum PetSpeciesEnum {
    DOG(1, "Dog"),
    CAT(2, "Cat"),
    BIRD(3, "Bird"),
    FISH(4, "Fish"),
    REPTILE(5, "Reptile"),
    OTHER(6, "Other");

    private final int id;
    private final String name;
}
