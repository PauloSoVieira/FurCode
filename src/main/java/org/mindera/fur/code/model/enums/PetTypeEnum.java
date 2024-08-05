package org.mindera.fur.code.model.enums;

public enum PetTypeEnum {
    DOG(1),
    CAT(2),
    BIRD(3),
    FISH(4),
    REPTILE(5);

    private final int id;

    PetTypeEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PetTypeEnum fromId(int id) {
        for (PetTypeEnum petType : values()) {
            if (petType.getId() == id) {
                return petType;
            }
        }
        throw new IllegalArgumentException("Invalid PetType id: " + id);
    }
}
