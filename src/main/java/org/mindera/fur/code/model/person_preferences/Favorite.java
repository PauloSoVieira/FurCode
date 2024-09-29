package org.mindera.fur.code.model.person_preferences;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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

    @NotNull(message = "Person must be provided")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @NotNull(message = "Pet must be provided")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @NotNull(message = "Favorite status must be provided")
    @PastOrPresent(message = "Favorite status must be in the past or present")
    @Column(name = "favorite_at", nullable = false)
    private LocalDateTime favoriteAt;

    @PrePersist
    protected void onCreate() {
        this.favoriteAt = LocalDateTime.now();
    }
}
