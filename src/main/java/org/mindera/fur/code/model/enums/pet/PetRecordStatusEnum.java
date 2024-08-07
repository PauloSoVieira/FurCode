package org.mindera.fur.code.model.enums.pet;

import lombok.Getter;

@Getter
public enum PetRecordStatusEnum {
    SPAYED_NEUTERED(1),
    HEALTHY(2),
    INJURED(3),
    SICK(4),
    DEAD(5);

    private final int id;

    PetRecordStatusEnum(int id) {
        this.id = id;
    }

    public static PetRecordStatusEnum fromId(int id) {
        for (PetRecordStatusEnum medicalRecordsStatus : values()) {
            if (medicalRecordsStatus.getId() == id) {
                return medicalRecordsStatus;
            }
        }
        throw new IllegalArgumentException("Invalid PetRecordsStatus id: " + id);
    }
}
