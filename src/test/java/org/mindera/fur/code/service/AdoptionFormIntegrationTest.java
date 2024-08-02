package org.mindera.fur.code.service;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.mindera.fur.code.dto.formsDTO.AdoptionFormCreateDTO;
import org.mindera.fur.code.dto.formsDTO.AdoptionFormDTO;
import org.mindera.fur.code.dto.formsDTO.FormFieldDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AdoptionFormIntegrationTest {


    @LocalServerPort
    private int port;

    @Autowired
    private AdoptionFormService adoptionFormService;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    public void tearDown() {
        adoptionFormService.deleteAllAdoptionForms();
    }

    @Test
    public void update_AdoptionFormById_return200() {
        AdoptionFormDTO adoptionForm = new AdoptionFormDTO(
                "jojo",
                1L,
                1L,
                1L,
                Set.of(new FormFieldDTO(1L, "1", "1")));

        String adoptionId = given()
                .contentType(ContentType.JSON)
                .body(adoptionForm)
                .when()
                .post("/api/v1/adoptionForm")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .jsonPath().getString("id");

        AdoptionFormDTO adoptionFormDTO = given()
                .contentType(ContentType.JSON)
                .body(adoptionForm)
                .when()
                .put("/api/v1/adoptionForm/" + adoptionId)
                .then()
                .statusCode(200)
                .extract().body().as(AdoptionFormDTO.class);

        adoptionForm.setName("Pablo");
        adoptionForm.setFormFields(Set.of(new FormFieldDTO(2L, "2", "2")));

        adoptionFormService.updateAdoptionForm(adoptionFormDTO, Long.valueOf(adoptionId));

        AdoptionFormDTO updatedForm = given()
                .when()
                .get("/api/v1/adoptionForm/" + adoptionId)
                .then()
                .statusCode(200)
                .extract().body().as(AdoptionFormDTO.class);


        // assertEquals("Pablo", updatedForm.getName());

    }

    @Nested
    class crudAdoptionForms {
        @Test
        public void create_AdoptionForm_returns201() {
            AdoptionFormCreateDTO adoptionForm = new AdoptionFormCreateDTO("jojo", 1L, 1L, 1L);


            AdoptionFormDTO adoptionFormDTO =
                    given()
                            .contentType(ContentType.JSON)
                            .body(adoptionForm)
                            .when()
                            .post("/api/v1/adoptionForm")
                            .then()
                            .statusCode(201)
                            .extract()
                            .body()
                            .as(AdoptionFormDTO.class);

            assertEquals("jojo", adoptionFormDTO.getName());
        }


        @Test
        public void get_AdoptionFormById_returns200() {
            AdoptionFormCreateDTO adoptionForm = new AdoptionFormCreateDTO("jojo", 1L, 1L, 1L);

            AdoptionFormDTO adoptionFormDTO =
                    given()
                            .contentType(ContentType.JSON)
                            .body(adoptionForm)
                            .when()
                            .post("/api/v1/adoptionForm")
                            .then()
                            .statusCode(201)
                            .extract()
                            .body()
                            .as(AdoptionFormDTO.class);


            String id = given()
                    .contentType(ContentType.JSON)
                    .body(adoptionForm)
                    .when()
                    .post("/api/v1/adoptionForm")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .jsonPath().getString("id");

            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/api/v1/adoptionForm/" + id)
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(AdoptionFormDTO.class);


            Assertions.assertEquals(adoptionForm.getName(), adoptionFormDTO.getName());

        }


        @Test
        public void getAll_AdoptionForm_return200() {
            AdoptionFormCreateDTO adoptionFormCreateDTO = new AdoptionFormCreateDTO("jojjo", 1L, 1L, 1L);

            AdoptionFormDTO adoptionFormDTO = given()
                    .contentType(ContentType.JSON)
                    .body(adoptionFormCreateDTO)
                    .when()
                    .post("/api/v1/adoptionForm")
                    .then()
                    .statusCode(201)
                    .extract().body().as(AdoptionFormDTO.class);

            List<AdoptionFormDTO> adoptionFormDTOList =
                    given()
                            .when()
                            .get("/api/v1/adoptionForm/all")
                            .then()
                            .statusCode(200)
                            .extract().body().jsonPath().getList(".", AdoptionFormDTO.class);


            Assertions.assertEquals(1, adoptionFormDTOList.size());


        }

        @Test
        public void delete_AdoptionFormById_return204() {
            AdoptionFormCreateDTO adoptionFormCreateDTO = new AdoptionFormCreateDTO("jojo", 1L, 1L, 1L);

            AdoptionFormDTO adoptionFormDTO = given()
                    .contentType(ContentType.JSON)
                    .body(adoptionFormCreateDTO)
                    .when()
                    .post("/api/v1/adoptionForm")
                    .then()
                    .statusCode(201)
                    .extract().body().as(AdoptionFormDTO.class);

            String id = given()
                    .contentType(ContentType.JSON)
                    .body(adoptionFormDTO)
                    .when()
                    .post("/api/v1/adoptionForm")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .jsonPath().getString("id");

            given()
                    .when()
                    .delete("/api/v1/adoptionForm/" + id)
                    .then()
                    .statusCode(204);

        }

    }


}

