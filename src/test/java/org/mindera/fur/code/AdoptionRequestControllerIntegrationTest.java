package org.mindera.fur.code;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.State;
import org.mindera.fur.code.service.AdoptionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AdoptionRequestControllerIntegrationTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    private AdoptionRequestService adoptionRequestService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        adoptionRequestService.deleteAllAdoptionRequests();
    }

    @Nested
    class crudAdoptionRequest {
        @Test
        void createAdoptionRequestShouldReturn201() {
            Date date = new Date();
//            Pet pet = new Pet();
//            pet.setId(1L);
            // Set other required pet fields
            Shelter shelter = new Shelter();
            shelter.setId(1L);
            // Set other required shelter fields
            Person person = new Person();
            person.setId(1L);
            // Set other required person fields
            AdoptionRequestCreationDTO adoptionRequestCreationDTO = new AdoptionRequestCreationDTO();
            adoptionRequestCreationDTO.setDate(date);
            adoptionRequestCreationDTO.setState(State.SENT);
            adoptionRequestCreationDTO.setPersonId(person.getId());
            adoptionRequestCreationDTO.setShelterId(shelter.getId());
//            adoptionRequestCreationDTO.setPetId(pet.getId());
            System.out.println("DTO: " + adoptionRequestCreationDTO);
            given()
                    .contentType(ContentType.JSON)
                    .body(adoptionRequestCreationDTO)
                    .when()
                    .post("/api/v1/adoption-request")
                    .then()
                    .statusCode(201)
                    .log().all();
        }

        @Test
        void getAdoptionRequestByIdShouldReturn200() {
            Date date = new Date();
            AdoptionRequestCreationDTO adoptionRequestCreationDTO = new AdoptionRequestCreationDTO(
                    1L,
                    1L,
                    1L,
                    State.SENT,
                    date
            );

            AdoptionRequestDTO adoptionRequestDTO =
                    given()
                            .contentType(ContentType.JSON)
                            .body(adoptionRequestCreationDTO)
                            .when()
                            .post("/api/v1/adoption-request")
                            .then()
                            .statusCode(201).extract().body().as(AdoptionRequestDTO.class);

            String adoptionRequestId =
                    given()
                            .contentType(ContentType.JSON)
                            .body(adoptionRequestDTO)
                            .when()
                            .post("/api/v1/adoption-request")
                            .then()
                            .statusCode(201).extract().body().jsonPath().getString("id");

            AdoptionRequestDTO findAdoptionRequestDTO =
                    given()
                            .when()
                            .get("/api/v1/adoption-request/" + adoptionRequestId)
                            .then()
                            .statusCode(200).extract().body().as(AdoptionRequestDTO.class);
        }

        @Test
        void getAllAdoptionRequestsShouldReturn200() {
            Date date = new Date();
            AdoptionRequestCreationDTO adoptionRequestCreationDTO = new AdoptionRequestCreationDTO(
                    1L,
                    1L,
                    1L,
                    State.SENT,
                    date
            );

            AdoptionRequestDTO adoptionRequestDTO =
                    given()
                            .contentType(ContentType.JSON)
                            .body(adoptionRequestCreationDTO)
                            .when()
                            .post("/api/v1/adoption-request")
                            .then()
                            .statusCode(201).extract().body().as(AdoptionRequestDTO.class);

            List<AdoptionRequestDTO> adoptionRequestDTOList =
                    given()
                            .when()
                            .get("/api/v1/adoption-request/all")
                            .then()
                            .statusCode(200).extract().body().jsonPath().getList(".", AdoptionRequestDTO.class);

            Assertions.assertEquals(1, adoptionRequestDTOList.size());
        }

        @Test
        void updateAdoptionRequestShouldReturn200() {
            Date date = new Date();
            AdoptionRequestCreationDTO adoptionRequestCreationDTO = new AdoptionRequestCreationDTO(
                    1L,
                    1L,
                    1L,
                    State.SENT,
                    date
            );

            AdoptionRequestDTO adoptionRequestDTO =
                    given()
                            .contentType(ContentType.JSON)
                            .body(adoptionRequestCreationDTO)
                            .when()
                            .post("/api/v1/adoption-request")
                            .then()
                            .statusCode(201)
                            .extract()
                            .body()
                            .as(AdoptionRequestDTO.class);

            String adoptionRequestId =
                    given()
                            .contentType(ContentType.JSON)
                            .body(adoptionRequestDTO)
                            .when()
                            .post("/api/v1/adoption-request")
                            .then()
                            .statusCode(201)
                            .extract()
                            .body()
                            .jsonPath()
                            .getString("id");

            adoptionRequestDTO.setState(State.SENT);
            given()
                    .contentType(ContentType.JSON)
                    .body(adoptionRequestDTO)
                    .patch("/api/v1/adoption-request/update/" + adoptionRequestId)
                    .then()
                    .statusCode(200).extract().body().as(AdoptionRequestDTO.class);

            Assertions.assertEquals(State.SENT, adoptionRequestDTO.getState());
        }

        @Test
        void deleteAdoptionRequestShouldReturn204() {
            Date date = new Date();
            AdoptionRequestCreationDTO adoptionRequestCreationDTO = new AdoptionRequestCreationDTO(
                    1L,
                    1L,
                    1L,
                    State.SENT,
                    date
            );

            AdoptionRequestDTO adoptionRequestDTO =
                    given()
                            .contentType(ContentType.JSON)
                            .body(adoptionRequestCreationDTO)
                            .when()
                            .post("/api/v1/adoption-request")
                            .then()
                            .statusCode(201).extract().body().as(AdoptionRequestDTO.class);

            String adoptionRequestId =
                    given()
                            .contentType(ContentType.JSON)
                            .body(adoptionRequestDTO)
                            .when()
                            .post("/api/v1/adoption-request")
                            .then()
                            .statusCode(201).extract().body().jsonPath().getString("id");

            given()
                    .when()
                    .delete("/api/v1/adoption-request/delete/" + adoptionRequestId)
                    .then()
                    .statusCode(204);
        }

        @Nested
        class validation {
            @Test
            void createAdoptionRequestWithNullAdoptionRequestId() {
                //TODO VALIDATION TESTS
            }
        }
    }
}
