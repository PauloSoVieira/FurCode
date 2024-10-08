package org.mindera.fur.code.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindera.fur.code.model.pet.Pet;

import java.time.LocalDate;
import java.util.Set;

/**
 * Class representing a Shelter.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "shelter")
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name can't be empty")
    @Size(max = 100, message = "Shelter name must be between 1 and 100 characters")
    @Column(nullable = false)
    private String name;

    @Pattern(regexp = "\\d+", message = "This field must contain only numbers")
    @NotBlank(message = "Vat can't be empty")
    @Size(max = 50, message = "Vat must be between 1 and 50 characters")
    @Column(nullable = false)
    private String vat;

    @NotBlank(message = "Email can't be empty")
    @Email(message = "Email must be valid")
    @Size(max = 50, message = "Email must be between 1 and 50 characters")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Address1 can't be empty")
    @Size(max = 200, message = "Address1 must be between 1 and 200 characters")
    @Column(nullable = false)
    private String address1;

    @NotBlank(message = "Address2 can't be empty")
    @Size(max = 200, message = "Address2 must be between 1 and 200 characters")
    @Column(nullable = false)
    private String address2;

    @Pattern(regexp = "\\d+", message = "This field must contain only numbers")
    @NotBlank(message = "Postal code can't be empty")
    @Size(max = 20, message = "Postal code must be between 1 and 20 characters")
    @Column(nullable = false)
    private String postalCode;

    @Pattern(regexp = "\\d+", message = "This field must contain only numbers")
    @NotBlank(message = "Phone can't be empty")
    @Size(max = 20, message = "Phone must be between 1 and 20 characters")
    @Column(nullable = false)
    private String phone;

    @Pattern(regexp = "\\d+", message = "This field must contain only numbers")
    @NotBlank(message = "Size can't be empty")
    @Size(max = 20, message = "Size must be between 1 and 20 characters")
    @Column(nullable = false)
    private String size;

    @NotNull(message = "IsActive can't be null")
    @Column(nullable = false)
    private Boolean isActive;

    @NotNull(message = "CreationDate can't be null")
    @Column(nullable = false)
    private LocalDate creationDate;

    @Column
    private String description;

    @Column
    private String facebookUrl;

    @Column
    private String instagramUrl;

    @Column
    private String webPageUrl;

    /**
     * The set of ShelterPersonRoles.
     */
    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Schema(description = "The collection of person roles associated with the shelter")
    private Set<ShelterPersonRoles> shelterPersonRoles;

    /**
     * The set of Pet.
     */
    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Schema(description = "The collection of pets associated with the shelter")
    private Set<Pet> pet;

    /**
     * The set of Donation.
     */
    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Schema(description = "The collection of donations associated with the shelter")
    private Set<Donation> donations;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    @Schema(description = "The person who donated")
    private Person person;
}
