package org.mindera.fur.code.repository;

import org.mindera.fur.code.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
