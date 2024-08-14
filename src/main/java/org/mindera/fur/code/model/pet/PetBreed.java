package org.mindera.fur.code.model.pet;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "pet_breed")
public class PetBreed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "External API ID must be provided")
    @Size(min = 1, max = 255, message = "External API ID must be between 1 and 255 characters")
    @Column(nullable = false)
    private String externalApiId;

    @NotBlank(message = "Breed name must be provided")
    @Size(min = 1, max = 255, message = "Pet breed name must be between 1 and 255 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Breed description must be provided")
    @Size(min = 1, max = 2048, message = "Pet breed description must be between 1 and 2048 characters")
    @Column(nullable = false, length = 2048)
    private String description;
}
