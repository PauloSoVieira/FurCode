package org.mindera.fur.code.model.pet;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * A model class for a pet chip.
 * <p>
 * <b>Note:</b> This class is currently not used in the application.
 * It is included for future reference and may be used in the future.
 * Maybe reference to medical records or other associated entities can be added later
 *</p>
 *
 */
@Data
@Entity
@Table(name = "pet_chip")
public class PetChip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Chip number must be provided")
    @Column(nullable = false, unique = true)
    private String chipNumber;

    @NotBlank(message = "Manufacturer must be provided")
    @Column(nullable = false)
    private String manufacturer;

    @Valid
    @NotNull(message = "Registration date must be provided")
    @Column(nullable = false)
    private Date registrationDate;

    @Valid
    @NotNull(message = "Last checked date must be provided")
    @Column(nullable = false)
    private Date lastCheckedDate;

    @NotNull(message = "Active status must be provided")
    @Column(nullable = false)
    private Boolean active;
}
