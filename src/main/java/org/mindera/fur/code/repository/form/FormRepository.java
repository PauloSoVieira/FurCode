package org.mindera.fur.code.repository.form;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.model.form.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Schema(name = "Form Repository", description = "Repository for managing Form entities")
@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    List<Form> findByType(String type);

}
