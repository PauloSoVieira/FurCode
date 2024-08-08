package org.mindera.fur.code.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "shelter_person_roles")
@Data
@Schema(description = "The shelter person roles")
public class ShelterPersonRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier of the shelter person roles", example = "1", required = true)
    private Long id;


    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Schema(description = "The role of the person", example = "ROLE_USER", required = true)
    private Role role;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", nullable = false)
    @Schema(description = "The person who has the role", required = true)
    private Person person;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shelter_id", nullable = false)
    @Schema(description = "The shelter where the person has the role", required = true)
    private Shelter shelter;
}
