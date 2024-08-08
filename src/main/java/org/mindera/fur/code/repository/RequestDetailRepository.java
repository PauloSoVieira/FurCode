package org.mindera.fur.code.repository;

import org.mindera.fur.code.model.RequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestDetailRepository extends JpaRepository<RequestDetail, Long> {
}
