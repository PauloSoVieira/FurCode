package org.mindera.fur.code.model.enums.pet;

import lombok.Getter;

@Getter
public enum PetSpeciesEnum {
    DOG(1, "Dog"),
    CAT(2, "Cat"),
    BIRD(3, "Bird"),
    FISH(4, "Fish"),
    REPTILE(5, "Reptile"),
    OTHER(6, "Other");

    private final int id;
    private final String name;

    PetSpeciesEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

//    PetTypeEnum(int id) {
//        this.id = id;
//    }
//
//    public static PetTypeEnum fromId(int id) {
//        for (PetTypeEnum petType : values()) {
//            if (petType.getId() == id) {
//                return petType;
//            }
//        }
//        throw new IllegalArgumentException("Invalid PetType id: " + id);
//    }
}
