package org.mindera.fur.code.model.enums;

public enum PetSizeEnum {
    SMALL(1),
    MEDIUM(2),
    LARGE(3);

    private final int id;

    PetSizeEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PetSizeEnum fromId(int id) {
        for (PetSizeEnum petSize : values()) {
            if (petSize.getId() == id) {
                return petSize;
            }
        }
        throw new IllegalArgumentException("Invalid PetSize id: " + id);
    }
}
