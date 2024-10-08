package org.mindera.fur.code.model.pet;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.mindera.fur.code.model.interfaces.SoftDeletable;

import java.time.LocalDateTime;

/**
 * A model class for a pet record.
 */
@Getter
@Setter
@Entity
@Table(name = "pet_record")
public class PetRecord implements SoftDeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Pet is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @NotBlank(message = "Intervention is required")
    @Size(max = 999, message = "Intervention cannot be longer than 999 characters")
    @Column(length = 999)
    private String intervention;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // SoftDeletable methods
    @Override
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    @Override
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
