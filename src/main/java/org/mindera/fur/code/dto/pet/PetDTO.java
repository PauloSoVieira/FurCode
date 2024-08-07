package org.mindera.fur.code.dto.pet;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PetDTO {

    private Long id;

    @NotBlank(message = "Pet name must be provided")
    @Size(min = 1, max = 30, message = "Pet name must be between 1 and 30 characters")
    private String name;

    @NotNull(message = "Pet typeId must be provided")
    //@Enumerated(EnumType.ORDINAL)
    private Long petTypeId; // changed to int-petTypeId from Pet-petType

    @NotNull(message = "Shelter ID must be provided")
    private Long shelterId;

    @NotNull(message = "Adopted status must be provided")
    private Boolean isAdopted;

    @NotNull(message = "Size must be provided")
    //@Enumerated(EnumType.STRING)
    private String size;

    @NotNull(message = "Pet weight must be provided")
    @DecimalMin(value = "0.01", message = "Pet weight must be greater than 0.01 kilos")
    @DecimalMax(value = "999.99", message = "Pet weight must be less than 999.99 kilos")
    private Double weight;

    @NotBlank(message = "Pet color must be provided")
    @Size(min = 3, max = 99, message = "Pet color must be between 3 and 99 characters")
    private String color;

    @NotNull(message = "Pet age must be provided")
    @Min(value = 1, message = "Pet age must be greater than 1")
    @Max(value = 99, message = "Pet age must be less than 99")
    private Integer age;

    @NotBlank(message = "Pet observation must be provided")
    @Size(min = 1, max = 999, message = "Pet observation must be between 1 and 999 characters")
    private String observations;

    // This field maybe not needed if the medical record are manage separately
    //private Long medicalRecordId;
}
