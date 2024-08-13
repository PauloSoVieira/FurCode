/*
package org.mindera.fur.code.model.pet;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import org.mindera.fur.code.model.enums.pet.PetCageSizeEnum;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "pet_cage")
public class PetCage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Cage number must be provided")
    @Column(nullable = false, unique = true)
    private String cageNumber;

    @NotBlank(message = "Location must be provided")
    @Column(nullable = false)
    private String location;

    @NotNull(message = "Size must be provided")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetCageSizeEnum size;

    @Valid
    @OneToMany(mappedBy = "cage")
    private List<Pet> occupants;

    @Valid
    @NotNull(message = "Last cleaned date is required")
    @PastOrPresent(message = "Last cleaned date cannot be in the future")
    @Column
    private Date lastCleanedDate;

    @Column(nullable = false)
    private Boolean maintenanceNeeded;

    // Reference to a maintenance schedule or other details can be added
}

 */
