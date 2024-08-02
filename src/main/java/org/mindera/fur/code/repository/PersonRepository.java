package org.mindera.fur.code.repository;

import org.mindera.fur.code.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface PersonRepository extends JpaRepository<Person, Long> {

    UserDetails findByEmail(String login);
}
