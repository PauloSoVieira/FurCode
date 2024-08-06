package org.mindera.fur.code.integrationTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mindera.fur.code.dto.forms.FormFieldCreateDTO;
import org.mindera.fur.code.dto.forms.FormFieldDTO;
import org.mindera.fur.code.service.FormFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class FormFieldServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FormFieldService formFieldService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        formFieldService.deleteAll();
    }

    @Nested
    class CrudTestFieldForm {
        @Test
        void testCreateField() {
            FormFieldCreateDTO formFieldDTO1 = new FormFieldCreateDTO("Name", "Text");

            given()
                    .contentType("application/json")
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(200);
        }

        @Test
        void deleteField() {
            FormFieldCreateDTO formFieldDTO1 = new FormFieldCreateDTO("Name", "Text");

            FormFieldDTO fieldDTO = given()
                    .contentType("application/json")
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(200)
                    .extract().body().as(FormFieldDTO.class);


            String fieldId = given()
                    .contentType(ContentType.JSON)
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(200)
                    .extract().body().jsonPath().getString("id");

            given()
                    .contentType("application/json")
                    .when()
                    .delete("/api/v1/field/delete/" + fieldId)
                    .then()
                    .statusCode(204);
        }


        @Test
        void getFieldById() {
            FormFieldCreateDTO formFieldDTO1 = new FormFieldCreateDTO("Name", "Text");

            FormFieldDTO fieldDTO = given()
                    .contentType("application/json")
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(200)
                    .extract().body().as(FormFieldDTO.class);

            Long fieldId = Long.valueOf(given()
                    .contentType(ContentType.JSON)
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(200)
                    .extract().body().jsonPath().getString("id"));

            FormFieldDTO fieldDTO1 = given()
                    .contentType("application/json")
                    .when()
                    .get("/api/v1/field/" + fieldId)
                    .then()
                    .statusCode(200)
                    .extract().body().as(FormFieldDTO.class);

            assertEquals(fieldDTO1.getId(), fieldId);

        }

        @Test
        void getAllFields() {
            FormFieldCreateDTO formFieldDTO1 = new FormFieldCreateDTO("Name", "Text");

            given()
                    .contentType("application/json")
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(200);


            List<FormFieldDTO> fieldDTO = given()
                    .when()
                    .get("/api/v1/field/all")
                    .then()
                    .statusCode(200)
                    .extract().body().jsonPath().getList(".", FormFieldDTO.class);

            assertEquals(1, fieldDTO.size());
        }


        @Test
        void updateField() {
            FormFieldCreateDTO formFieldDTO1 = new FormFieldCreateDTO("Name", "Text");

            String fieldId = given()
                    .contentType(ContentType.JSON)
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(200)
                    .extract().body().jsonPath().getString("id");


            FormFieldDTO updateDTO = new FormFieldDTO(Long.parseLong(fieldId), "Test", "Text");

            FormFieldDTO fieldDTO = given()
                    .contentType("application/json")
                    .body(updateDTO)
                    .when()
                    .put("/api/v1/field/update/" + fieldId)
                    .then()
                    .statusCode(200)
                    .extract().body().as(FormFieldDTO.class);

            assertEquals("Test", fieldDTO.getName());

        }
    }

    @Nested
    class ValidationTestFieldForm {
        @Test
        void test_Validation_NameNull_Field() {
            FormFieldCreateDTO formFieldDTO1 = new FormFieldCreateDTO(null, "Text");

            given()
                    .contentType("application/json")
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(400);
        }

        @Test
        void test_Validation_NameEmpty_Field() {
            FormFieldCreateDTO formFieldDTO1 = new FormFieldCreateDTO("", "Text");

            given()
                    .contentType("application/json")
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(400);
        }

        @Test
        void test_Validation_TypeNull_Field() {
            FormFieldCreateDTO formFieldDTO1 = new FormFieldCreateDTO("Name", null);

            given()
                    .contentType("application/json")
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(400);
        }

        @Test
        void test_Validation_TypeEmpty_Field() {
            FormFieldCreateDTO formFieldDTO1 = new FormFieldCreateDTO("Name", "");

            given()
                    .contentType("application/json")
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(400);
        }

        @Test
        public void Return400_FieldName_IsTooLong() {
            FormFieldDTO formFieldDTO1 = new FormFieldDTO();
            formFieldDTO1.setName(StringUtils.repeat("a", 201));
            formFieldDTO1.setType("Text");

            given()
                    .contentType("application/json")
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(400);
        }

        @Test
        public void Return400_FieldName_IsTooShort() {
            FormFieldDTO formFieldDTO1 = new FormFieldDTO();
            formFieldDTO1.setName(StringUtils.repeat("a", 1));
            formFieldDTO1.setType("Text");

            given()
                    .contentType("application/json")
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(400);
        }

        @Test
        public void Return400_FieldType_IsTooShort() {
            FormFieldDTO formFieldDTO1 = new FormFieldDTO();
            formFieldDTO1.setName("Name");
            formFieldDTO1.setType(StringUtils.repeat("a", 1));

            given()
                    .contentType("application/json")
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(400);
        }

        @Test
        public void Return400_FieldType_IsTooLong() {
            FormFieldDTO formFieldDTO1 = new FormFieldDTO();
            formFieldDTO1.setName("Name");
            formFieldDTO1.setType(StringUtils.repeat("a", 101));

            given()
                    .contentType("application/json")
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(400);
        }
    }

}