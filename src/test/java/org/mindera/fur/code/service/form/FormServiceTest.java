package org.mindera.fur.code.service.form;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.mindera.fur.code.dto.form.FormCreateDTO;
import org.mindera.fur.code.dto.form.FormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
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
    class CrudForms {
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


        }

        @Test
        void getForm_returns200() {
            FormCreateDTO formcreateDTO = new FormCreateDTO();
            formcreateDTO.setName("Test Form");
            formcreateDTO.setCreatedAt(LocalDateTime.now());
            formcreateDTO.setType("DEFAULT");
            formcreateDTO.setFormFieldAnswers(new ArrayList<>());

            String formIdString = given()
                    .contentType(ContentType.JSON)
                    .body(formcreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .jsonPath()
                    .getString("id");

            Long formId = Long.parseLong(formIdString);

            FormDTO getFormDTO = given()
                    .when()
                    .get("/api/v1/forms/" + formId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(FormDTO.class);

            Assertions.assertEquals(formId, getFormDTO.getId());
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
                    .body();

            List<FormDTO> forms = given()
                    .when()
                    .get("/api/v1/forms/all")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .jsonPath()
                    .getList(".", FormDTO.class);

            Assertions.assertEquals(1, forms.size());
        }

        @Test
        void deleteForm_returns204() {
            FormCreateDTO formcreateDTO = new FormCreateDTO();
            formcreateDTO.setName("Test Form");
            formcreateDTO.setCreatedAt(LocalDateTime.now());
            formcreateDTO.setType("DEFAULT");
            formcreateDTO.setFormFieldAnswers(new ArrayList<>());

            String formIdString = given()
                    .contentType(ContentType.JSON)
                    .body(formcreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .jsonPath()
                    .getString("id");

            Long formId = Long.parseLong(formIdString);

            given()
                    .contentType(ContentType.JSON)
                    .body(formcreateDTO)
                    .when()
                    .delete("/api/v1/forms/" + formId)
                    .then()
                    .statusCode(204);
        }
    }
}