package org.mindera.fur.code.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.interfaces.SoftDeletable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

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

    /**
     * Retrieves all soft-deleted entities.
     *
     * @return List of soft-deleted entities.
     */
    default List<T> findAllDeleted() {
        return findAllByDeletedAtIsNotNull();
    }

    /**
     * Retrieves a soft-deleted entity by its ID.
     *
     * @param id The ID of the entity.
     * @return An Optional containing the entity if found and deleted.
     */
    default Optional<T> findDeletedById(ID id) {
        return findByIdAndDeletedAtIsNotNull(id);
    }

    /**
     * Retrieves an entity by its ID, including soft-deleted ones.
     *
     * @param id The ID of the entity.
     * @return An Optional containing the entity if found.
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id")
    Optional<T> findByIdIncludingDeleted(@Param("id") ID id);

    /**
     * Retrieves all entities, including soft-deleted ones.
     *
     * @return List of all entities.
     */
    @Query("SELECT e FROM #{#entityName} e")
    List<T> findAllIncludingDeleted();

    List<T> findAllByDeletedAtIsNull();

    Optional<T> findByIdAndDeletedAtIsNull(ID id);

    List<T> findAllByDeletedAtIsNotNull();

    Optional<T> findByIdAndDeletedAtIsNotNull(ID id);
}
