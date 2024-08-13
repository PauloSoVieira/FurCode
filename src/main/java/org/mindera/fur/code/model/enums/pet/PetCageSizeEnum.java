package org.mindera.fur.code.model.enums.pet;

import lombok.Getter;

@Getter
public enum PetCageSizeEnum {
    SMALL(1, "Small"),
    MEDIUM(2, "Medium"),
    LARGE(3, "Large");

    private final int id;
    private final String name;

    PetCageSizeEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

//    public static PetSizeEnum fromId(int id) {
//        for (PetSizeEnum petSize : values()) {
//            if (petSize.getId() == id) {
//                return petSize;
//            }
//        }
//        throw new IllegalArgumentException("Invalid PetSize id: " + id);
//    }
}
