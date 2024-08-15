package org.mindera.fur.code.model;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.mindera.fur.code.model.pet.Pet;

import java.time.LocalDate;
import java.util.Set;

/**
 * Class representing a Shelter.
 */
@Entity
@Data
@Table(name = "shelter")
@Tag(name = "Shelter", description = "Details about the shelter entity")

public class Shelter {

    @Id
    @Positive
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier of the shelter", example = "1", required = true)
    private Long id;
    @Schema(description = "The name of the shelter", example = "Shelter 1", required = true)
    @Size(min = 1, max = 100, message = "Shelter name must be between 1 and 100 characters")
    private String name;
    @Schema(description = "The vat of the shelter", example = "12345", required = true)
    private Integer vat;
    @Schema(description = "The email of the shelter", example = "shelter@example.com", required = true)
    @Size(min = 1, max = 50, message = "Email must be between 1 and 50 characters")
    private String email;
    @Schema(description = "The address1 of the shelter", example = "123 Main Street", required = true)
    @Size(min = 1, max = 200, message = "Address1 must be between 1 and 200 characters")
    private String address1;
    @Schema(description = "The address2 of the shelter", example = "Apartment 1", required = true)
    @Size(min = 1, max = 200, message = "Address2 must be between 1 and 200 characters")
    private String address2;
    @Schema(description = "The postal code of the shelter", example = "12345", required = true)
    private String postalCode;
    @Schema(description = "The phone of the shelter", example = "1234567890", required = true)
    private Integer phone;
    @Schema(description = "The size of the shelter", example = "10", required = true)
    @Min(value = 1, message = "Size must be greater than or equal to 1")
    @Max(value = 1000, message = "Size must be less than or equal to 1000")
    private Integer size;
    @Schema(description = "The is active of the shelter", example = "true", required = true)
    private Boolean isActive;
    @Schema(description = "The creation date of the shelter", example = "2023-01-01", required = true)
    private LocalDate creationDate;

    /**
     * The set of ShelterPersonRoles.
     */
    @Schema(description = "The unique identifier of the shelter", example = "1", required = true)
    @OneToMany(mappedBy = "shelter", fetch = FetchType.LAZY)
    private Set<ShelterPersonRoles> shelterPersonRoles;

    /**
     * The set of Pet.
     */
    @Schema(description = "The unique identifier of the shelter", example = "1", required = true)
    @OneToMany(mappedBy = "shelter")
    private Set<Pet> pet;
    /**
     * The set of Donation.
     */
    @Schema(description = "The unique identifier of the shelter", example = "1", required = true)
    @OneToMany(mappedBy = "shelter")
    private Set<Donation> donations;
}
