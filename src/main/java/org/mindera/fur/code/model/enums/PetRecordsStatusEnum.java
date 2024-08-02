package org.mindera.fur.code.model.enums;

public enum PetRecordsStatusEnum {
    SPAYED_NEUTERED(1),
    HEALTHY(2),
    INJURED(3),
    SICK(4),
    DEAD(5);

    private final int id;

    PetRecordsStatusEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PetRecordsStatusEnum fromId(int id) {
        for (PetRecordsStatusEnum medicalRecordsStatus : values()) {
            if (medicalRecordsStatus.getId() == id) {
                return medicalRecordsStatus;
            }
        }
        throw new IllegalArgumentException("Invalid MedicalRecordsStatus id: " + id);
    }
}
