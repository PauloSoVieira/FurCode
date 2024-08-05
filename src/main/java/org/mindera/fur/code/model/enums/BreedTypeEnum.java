package org.mindera.fur.code.model.enums;

public enum BreedTypeEnum {
    LABRADOR(1),
    BEAGLE(2),
    BULLDOG(3),
    CHIHUAHUA(4),
    SHEPHERD(5);

    private final int id;

    BreedTypeEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static BreedTypeEnum fromId(int id) {
        for (BreedTypeEnum breedType : values()) {
            if (breedType.getId() == id) {
                return breedType;
            }
        }
        throw new IllegalArgumentException("Invalid BreedType id: " + id);
    }
}
