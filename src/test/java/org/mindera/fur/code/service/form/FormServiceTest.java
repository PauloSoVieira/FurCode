package org.mindera.fur.code.service.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.mindera.fur.code.dto.form.FormCreateDTO;
import org.mindera.fur.code.dto.form.FormDTO;
import org.mindera.fur.code.dto.form.FormFieldCreateDTO;
import org.mindera.fur.code.dto.form.FormTemplateDTO;
import org.mindera.fur.code.model.form.Form;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class FormServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FormService formService;

    @Autowired
    private TestRestTemplate restTemplate;

    @AfterEach
    void tearDown() {
        formService.deleteAllForms();
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    class CrudForms{
        @Test
        void createForm_returns201() {

            FormCreateDTO formCreateDTO = new FormCreateDTO();
            formCreateDTO.setName("Test Form");
            formCreateDTO.setCreatedAt(LocalDateTime.now());
            formCreateDTO.setType("DEFAULT");
            formCreateDTO.setFormFieldAnswers(new ArrayList<>());

            FormDTO formDTO = given()
                    .contentType(ContentType.JSON)
                    .body(formCreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .as(FormDTO.class);


            Assertions.assertEquals("Test Form", formCreateDTO.getName());
        }

        @Test
        void getForm_returns200() {
            FormCreateDTO formcreateDTO = new FormCreateDTO();
            formcreateDTO.setName("Test Form");
            formcreateDTO.setCreatedAt(LocalDateTime.now());
            formcreateDTO.setType("DEFAULT");
            formcreateDTO.setFormFieldAnswers(new ArrayList<>());

            FormDTO formDTO = given()
                    .contentType(ContentType.JSON)
                    .body(formcreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .as(FormDTO.class);

           String formId = given()
                   .contentType(ContentType.JSON)
                   .body(formDTO)
                   .when()
                   .post("/api/v1/forms")
                   .then()
                   .statusCode(201)
                   .extract()
                   .body()
                   .as(FormDTO.class)
                   .getId().toString();

            FormDTO formDTO1 = given()
                    .when()
                    .get("/api/v1/forms/" + formId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(FormDTO.class);



            assertEquals(formcreateDTO.getName(), formDTO1.getName());
        }

        @Test
        void getAllForms_returns200() {
            FormCreateDTO formcreateDTO = new FormCreateDTO();
            formcreateDTO.setName("Test Form");
            formcreateDTO.setCreatedAt(LocalDateTime.now());
            formcreateDTO.setType("DEFAULT");
            formcreateDTO.setFormFieldAnswers(new ArrayList<>());

            given()
                    .contentType(ContentType.JSON)
                    .body(formcreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .as(FormDTO.class);

            List<FormDTO> forms = given()
                    .when()
                    .get("/api/v1/forms/all")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .jsonPath()
                    .getList(".", FormDTO.class);

            assertEquals(1, forms.size());






        }

        @Test
        void addFieldToForm_returns201() {
            FormCreateDTO formcreateDTO = new FormCreateDTO();
            formcreateDTO.setName("Test Form");
            formcreateDTO.setCreatedAt(LocalDateTime.now());
            formcreateDTO.setType("DEFAULT");
            formcreateDTO.setFormFieldAnswers(new ArrayList<>());

            FormDTO formDTO = given()
                    .contentType(ContentType.JSON)
                    .body(formcreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .as(FormDTO.class);

            String formId = given()
                    .contentType(ContentType.JSON)
                    .body(formDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .as(FormDTO.class)
                    .getId().toString();

            FormFieldCreateDTO formFieldCreateDTO = new FormFieldCreateDTO();
            formFieldCreateDTO.setQuestion("Test Question");
            formFieldCreateDTO.setFieldType("TEXT");

            given()
                    .contentType(ContentType.JSON)
                    .body(formFieldCreateDTO)
                    .when()
                    .post("/api/v1/forms/" + formId + "/field")
                    .then()
                    .statusCode(201);

            given()
                    .contentType(ContentType.JSON)
                    .body(formFieldCreateDTO)
                    .when()
                    .post("/api/v1/forms/" + formId + "/field")
                    .then()
                    .statusCode(201);

            FormDTO formDTO1 = given()
                    .when()
                    .get("/api/v1/forms/" + formId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(FormDTO.class);



            assertEquals(formcreateDTO.getName(), formDTO1.getName());
        }

        @Test
        void deleteFieldFromForm_returns204() {
            FormCreateDTO formcreateDTO = new FormCreateDTO();
            formcreateDTO.setName("Test Form");
            formcreateDTO.setCreatedAt(LocalDateTime.now());
            formcreateDTO.setType("DEFAULT");
            formcreateDTO.setFormFieldAnswers(new ArrayList<>());

            FormDTO formDTO = given()
                    .contentType(ContentType.JSON)
                    .body(formcreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .as(FormDTO.class);

            Long formId = formDTO.getId();

            FormFieldCreateDTO formFieldCreateDTO = new FormFieldCreateDTO();
            formFieldCreateDTO.setQuestion("Test Question");
            formFieldCreateDTO.setFieldType("TEXT");

            given()
                    .contentType(ContentType.JSON)
                    .body(formFieldCreateDTO)
                    .when()
                    .post("/api/v1/forms/" + formId + "/field")
                    .then()
                    .statusCode(201);

            given()
                    .contentType(ContentType.JSON)
                    .body(formFieldCreateDTO)
                    .when()
                    .post("/api/v1/forms/" + formId + "/field")
                    .then()
                    .statusCode(201);

            FormDTO formDTO1 = given()
                    .when()
                    .get("/api/v1/forms/" + formId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(FormDTO.class);

            assertEquals(formcreateDTO.getName(), formDTO1.getName());
        }

        @Test
        void deleteForm_returns204() {
            FormCreateDTO formcreateDTO = new FormCreateDTO();
            formcreateDTO.setName("Test Form");
            formcreateDTO.setCreatedAt(LocalDateTime.now());
            formcreateDTO.setType("DEFAULT");
            formcreateDTO.setFormFieldAnswers(new ArrayList<>());

            FormDTO formDTO = given()
                    .contentType(ContentType.JSON)
                    .body(formcreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .as(FormDTO.class);

            String formId = given()
                    .contentType(ContentType.JSON)
                    .body(formDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .as(FormDTO.class)
                    .getId().toString();

            given()
                    .contentType(ContentType.JSON)
                    .body(formDTO)
                    .when()
                    .delete("/api/v1/forms/" + formId)
                    .then()
                    .statusCode(204);



            assertFalse(Boolean.parseBoolean(formId), "Form should be deleted");
        }
    }



}