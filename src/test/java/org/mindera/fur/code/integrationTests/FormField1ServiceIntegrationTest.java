package org.mindera.fur.code.integrationTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mindera.fur.code.dto.forms.FormFieldCreateDTO1;
import org.mindera.fur.code.dto.forms.FormFieldDTO1;
import org.mindera.fur.code.service.FormFieldService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class FormField1ServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FormFieldService2 formFieldService2;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        formFieldService2.deleteAll();
    }

    @Nested
    class CrudTestFieldForm {
        @Test
        void testCreateField() {
            FormFieldCreateDTO1 formFieldDTO1 = new FormFieldCreateDTO1("Name", "Text");

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
            FormFieldCreateDTO1 formFieldDTO1 = new FormFieldCreateDTO1("Name", "Text");

            FormFieldDTO1 fieldDTO = given()
                    .contentType("application/json")
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(200)
                    .extract().body().as(FormFieldDTO1.class);


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
            FormFieldCreateDTO1 formFieldDTO1 = new FormFieldCreateDTO1("Name", "Text");

            FormFieldDTO1 fieldDTO = given()
                    .contentType("application/json")
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(200)
                    .extract().body().as(FormFieldDTO1.class);

            Long fieldId = Long.valueOf(given()
                    .contentType(ContentType.JSON)
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(200)
                    .extract().body().jsonPath().getString("id"));

            FormFieldDTO1 fieldDTO1 = given()
                    .contentType("application/json")
                    .when()
                    .get("/api/v1/field/" + fieldId)
                    .then()
                    .statusCode(200)
                    .extract().body().as(FormFieldDTO1.class);

            assertEquals(fieldDTO1.getId(), fieldId);

        }

        @Test
        void getAllFields() {
            FormFieldCreateDTO1 formFieldDTO1 = new FormFieldCreateDTO1("Name", "Text");

            given()
                    .contentType("application/json")
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(200);


            List<FormFieldDTO1> fieldDTO = given()
                    .when()
                    .get("/api/v1/field/all")
                    .then()
                    .statusCode(200)
                    .extract().body().jsonPath().getList(".", FormFieldDTO1.class);

            assertEquals(1, fieldDTO.size());
        }


        @Test
        void updateField() {
            FormFieldCreateDTO1 formFieldDTO1 = new FormFieldCreateDTO1("Name", "Text");

            String fieldId = given()
                    .contentType(ContentType.JSON)
                    .body(formFieldDTO1)
                    .when()
                    .post("/api/v1/field")
                    .then()
                    .statusCode(200)
                    .extract().body().jsonPath().getString("id");


            FormFieldDTO1 updateDTO = new FormFieldDTO1(Long.parseLong(fieldId), "Test", "Text");

            FormFieldDTO1 fieldDTO = given()
                    .contentType("application/json")
                    .body(updateDTO)
                    .when()
                    .put("/api/v1/field/update/" + fieldId)
                    .then()
                    .statusCode(200)
                    .extract().body().as(FormFieldDTO1.class);

            assertEquals("Test", fieldDTO.getName());

        }
    }

    @Nested
    class ValidationTestFieldForm {
        @Test
        void test_Validation_NameNull_Field() {
            FormFieldCreateDTO1 formFieldDTO1 = new FormFieldCreateDTO1(null, "Text");

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
            FormFieldCreateDTO1 formFieldDTO1 = new FormFieldCreateDTO1("", "Text");

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
            FormFieldCreateDTO1 formFieldDTO1 = new FormFieldCreateDTO1("Name", null);

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
            FormFieldCreateDTO1 formFieldDTO1 = new FormFieldCreateDTO1("Name", "");

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
            FormFieldDTO1 formFieldDTO1 = new FormFieldDTO1();
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
            FormFieldDTO1 formFieldDTO1 = new FormFieldDTO1();
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
            FormFieldDTO1 formFieldDTO1 = new FormFieldDTO1();
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
            FormFieldDTO1 formFieldDTO1 = new FormFieldDTO1();
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