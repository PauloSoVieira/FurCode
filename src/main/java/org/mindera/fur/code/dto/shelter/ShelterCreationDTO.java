package org.mindera.fur.code.dto.shelter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for creating a shelter.
 */
@Data
@Schema(description = "A shelter creation request")
@Tag(name = "Shelter Creation", description = "Details about the shelter creation request")
public class ShelterCreationDTO {
    @NotNull
    @NotBlank
    @Schema(description = "The name of the shelter", example = "The name of the shelter", required = true)
    private String name;

    @NotNull
    @Positive
    @Schema(description = "The vat number of the shelter", example = "123456789", required = true)
    private Long vat;

    @NotNull
    @NotBlank
    @Schema(description = "The email of the shelter", example = "email@email.com", required = true)
    private String email;

    @NotNull
    @NotBlank
    @Schema(description = "The address1 of the shelter", example = "The address1 of the shelter", required = true)
    private String address1;

    @Schema(description = "The address2 of the shelter", example = "The address2 of the shelter")
    private String address2;

    @NotNull
    @NotBlank
    @Schema(description = "The postal code of the shelter", example = "12345", required = true)
    private String postalCode;

    @NotNull
    @Schema(description = "The phone number of the shelter", example = "123456789", required = true)
    private Long phone;

    @NotNull
    @Schema(description = "The size of the shelter", example = "1", required = true)
    private Long size;

    @NotNull
    @Schema(description = "The isActive status of the shelter", example = "true", required = true)
    private Boolean isActive;

    @NotNull
    @NotBlank
    @Schema(description = "The creation date of the shelter", example = "2023-01-01", required = true)
    private LocalDate creationDate;

    /**
     * Constructor with parameters.
     *
     * @param name         The name of the shelter.
     * @param vat          The vat number of the shelter.
     * @param email        The email of the shelter.
     * @param address1     The address1 of the shelter.
     * @param address2     The address2 of the shelter.
     * @param postalCode   The postal code of the shelter.
     * @param phone        The phone number of the shelter.
     * @param size         The size of the shelter.
     * @param isActive     The isActive status of the shelter.
     * @param creationDate The creation date of the shelter.
     */
    public ShelterCreationDTO(String name, Long vat, String email, String address1, String address2, String postalCode, Long phone, Long size, Boolean isActive, LocalDate creationDate) {
        this.name = name;
        this.vat = vat;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.postalCode = postalCode;
        this.phone = phone;
        this.size = size;
        this.isActive = isActive;
        this.creationDate = creationDate;
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
