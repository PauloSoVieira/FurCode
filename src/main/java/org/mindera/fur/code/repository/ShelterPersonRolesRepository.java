package org.mindera.fur.code.repository;

import org.mindera.fur.code.model.ShelterPersonRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelterPersonRolesRepository extends JpaRepository<ShelterPersonRoles, Long> {
}
