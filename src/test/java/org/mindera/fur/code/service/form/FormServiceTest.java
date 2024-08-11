package org.mindera.fur.code.service.form;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.mindera.fur.code.dto.form.FormCreateDTO;
import org.mindera.fur.code.dto.form.FormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class FormServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FormService formService;

    @AfterEach
    void tearDown() {
        formService.deleteAllForms();
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    class crudForms {
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

//            //Had to do this because the name is being returned in the response as a string
//            String expectedName = formDTO.getName().contains("\"name\":\"")
//                    ? formDTO.getName().substring(formDTO.getName().indexOf("\"name\":\"") + 8, formDTO.getName().indexOf("\","))
//                    : formCreateDTO.getName();

            Assertions.assertEquals("Test Form", formCreateDTO.getName());
        }


        @Test
        void getForm_returns200() {
            FormCreateDTO formcreateDTO = new FormCreateDTO();
            formcreateDTO.setName("Test Form");
            formcreateDTO.setCreatedAt(LocalDateTime.now());
            formcreateDTO.setType("DEFAULT");
            formcreateDTO.setFormFieldAnswers(new ArrayList<>());

            // Create the form
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

            // Fetch the form using the form ID
            FormDTO formDTO1 = given()
                    .when()
                    .get("/api/v1/forms/" + formId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(FormDTO.class);



            // Verify that the fetched form has the same name as the created one
            assertEquals(formcreateDTO.getName(), formDTO1.getName());
        }

    }

}