package org.mindera.fur.code.model.interfaces;

import java.time.LocalDateTime;

public interface SoftDeletable {

    /**
     * Gets the deletion timestamp.
     *
     * @return the deletion timestamp, or null if not deleted.
     */
    LocalDateTime getDeletedAt();

    /**
     * Sets the deletion timestamp.
     *
     * @param deletedAt the timestamp when the entity was deleted.
     */
    void setDeletedAt(LocalDateTime deletedAt);
}
