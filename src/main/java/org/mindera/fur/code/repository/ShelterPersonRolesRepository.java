package org.mindera.fur.code.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.ShelterPersonRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Schema(description = "The shelter person roles repository")
public interface ShelterPersonRolesRepository extends JpaRepository<ShelterPersonRoles, Long> {
}
