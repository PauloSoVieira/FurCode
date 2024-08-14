package org.mindera.fur.code.model.enums.pet;

import lombok.Getter;

@Getter
public enum PetSizeEnum {
    SMALL(1, "Small"),
    MEDIUM(2, "Medium"),
    LARGE(3, "Large");

    private final int id;
    private final String name;

    PetSizeEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
