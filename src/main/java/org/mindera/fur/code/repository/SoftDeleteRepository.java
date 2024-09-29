package org.mindera.fur.code.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.interfaces.SoftDeletable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@Schema(description = "Soft delete repository")
@NoRepositoryBean
public interface SoftDeleteRepository<T extends SoftDeletable, ID> extends JpaRepository<T, ID> {

    /**
     * Retrieves all active (non-deleted) entities.
     *
     * @return List of active entities.
     */
    default List<T> findAllActive() {
        return findAllByDeletedAtIsNull();
    }

    /**
     * Retrieves an active (non-deleted) entity by its ID.
     *
     * @param id The ID of the entity.
     * @return An Optional containing the entity if found and not deleted.
     */
    default Optional<T> findActiveById(ID id) {
        return findByIdAndDeletedAtIsNull(id);
    }

    List<T> findAllByDeletedAtIsNull();

    Optional<T> findByIdAndDeletedAtIsNull(ID id);
}
