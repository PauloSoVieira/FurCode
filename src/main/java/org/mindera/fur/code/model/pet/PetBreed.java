package org.mindera.fur.code.model.pet;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "breed")
public class PetBreed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Breed name must be provided")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Breed description must be provided")
    @Column(nullable = false, length = 2048)
    private String description;
}
