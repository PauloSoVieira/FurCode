package org.mindera.fur.code.model.person_preferences;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.model.pet.Pet;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "favorites", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"person_id", "pet_id"})
})
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Schema(description = "The date and time the pet was favorited")
    @Column(name = "favorited_at", nullable = false)
    private LocalDateTime favoritedAt;
}
