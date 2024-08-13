package org.mindera.fur.code.model.pet;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.mindera.fur.code.model.enums.pet.PetTypeEnum;

@Data
@Entity
@Table(name = "pet_type")
public class PetType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Valid
    @NotNull(message = "Pet type name must be provided")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetTypeEnum type;

    @Valid
    @NotNull
    @ManyToOne
    @JoinColumn(name = "breed_id", nullable = false)
    private PetBreed breed;
}
