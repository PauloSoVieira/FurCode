package org.mindera.fur.code.repository.formTest;

import org.mindera.fur.code.model.formTest.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    List<Form> findByType(String type);

}
