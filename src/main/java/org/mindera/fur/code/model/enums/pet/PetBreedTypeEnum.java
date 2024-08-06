package org.mindera.fur.code.model.enums.pet;

public enum PetBreedTypeEnum {
    LABRADOR(1),
    BEAGLE(2),
    BULLDOG(3),
    CHIHUAHUA(4),
    SHEPHERD(5);

    private final int id;

    PetBreedTypeEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PetBreedTypeEnum fromId(int id) {
        for (PetBreedTypeEnum breedType : values()) {
            if (breedType.getId() == id) {
                return breedType;
            }
        }
        throw new IllegalArgumentException("Invalid BreedType id: " + id);
    }
}
