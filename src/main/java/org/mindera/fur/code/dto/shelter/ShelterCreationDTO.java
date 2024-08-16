package org.mindera.fur.code.dto.shelter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Date;

/**
 * DTO for creating a shelter.
 */
@Data
public class ShelterCreationDTO {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Long vat;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String address1;

    private String address2;

    @NotNull
    @NotBlank
    private String postalCode;

    @NotNull
    private Long phone;

    @NotNull
    private Long size;

    @NotNull
    private Boolean isActive;

    @NotNull
    private Date createdAt;

    /**
     * Constructor with parameters.
     *
     * @param name       The name of the shelter.
     * @param vat        The vat number of the shelter.
     * @param email      The email of the shelter.
     * @param address1   The address1 of the shelter.
     * @param address2   The address2 of the shelter.
     * @param postalCode The postal code of the shelter.
     * @param phone      The phone number of the shelter.
     * @param size       The size of the shelter.
     * @param isActive   The isActive status of the shelter.
     * @param createdAt  The creation date of the shelter.
     */
    public ShelterCreationDTO(String name, Long vat, String email, String address1, String address2, String postalCode, Long phone, Long size, Boolean isActive, Date createdAt) {
        this.name = name;
        this.vat = vat;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.postalCode = postalCode;
        this.phone = phone;
        this.size = size;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    /**
     * Default constructor.
     */
    public ShelterCreationDTO() {
    }

    /**
     * Constructor with parameters.
     *
     * @param name The name of the shelter.
     */
    public ShelterCreationDTO(String name) {
        this.name = name;
    }


}
