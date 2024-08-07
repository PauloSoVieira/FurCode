package org.mindera.fur.code.model;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.Data;
import org.mindera.fur.code.model.pet.Pet;

import java.util.Set;

@Entity
@Data
@Table(name = "shelter")
@Tag(name = "Shelter", description = "Details about the shelter entity")

public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier of the shelter", example = "1", required = true)

    private Long id;


    @Schema(description = "The name of the shelter", example = "Shelter 1", required = true)
    private String name;
    @Schema(description = "The vat of the shelter", example = "12345", required = true)
    private Integer vat;
    @Schema(description = "The email of the shelter", example = "shelter@example.com", required = true)
    private String email;
    @Schema(description = "The address1 of the shelter", example = "123 Main Street", required = true)
    private String address1;
    @Schema(description = "The address2 of the shelter", example = "Apartment 1", required = true)
    private String address2;
    @Schema(description = "The post code of the shelter", example = "12345", required = true)
    private String postCode;
    @Schema(description = "The phone of the shelter", example = "1234567890", required = true)
    private Integer phone;
    @Schema(description = "The size of the shelter", example = "10", required = true)
    private Integer size;
    @Schema(description = "The is active of the shelter", example = "true", required = true)
    private Boolean isActive;

    @Schema(description = "The unique identifier of the shelter", example = "1", required = true)
    @OneToMany(mappedBy = "shelter", fetch = FetchType.EAGER)
    private Set<ShelterPersonRoles> shelterPersonRoles;

    @Schema(description = "The unique identifier of the shelter", example = "1", required = true)
    @OneToMany(mappedBy = "shelter")
    private Set<Pet> pet;
    @Schema(description = "The unique identifier of the shelter", example = "1", required = true)
    @OneToMany(mappedBy = "shelter")
    private Set<Donation> donations;
}
