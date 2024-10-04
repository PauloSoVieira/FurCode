/*
package org.mindera.fur.code;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.model.Role;
import org.mindera.fur.code.service.PersonService;
import org.mindera.fur.code.service.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")

public class ShelterControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private ShelterService shelterService;

    @Autowired
    private PersonService personService;

    private String adminToken;
    private String managerToken;
    private String userToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        personService.deleteAllPersons();
        shelterService.deleteAllShelters();
        createTestUsers();
    }

    @AfterEach
    void tearDown() {
        shelterService.deleteAllShelters();
        personService.deleteAllPersons();
    }

    private void createTestUsers() {
        adminToken = createUserAndGetToken("admin@example.com", "adminpass", Role.ADMIN);
        managerToken = createUserAndGetToken("manager@example.com", "managerpass", Role.MANAGER);
        userToken = createUserAndGetToken("user@example.com", "userpass", Role.USER);
    }

    private String createUserAndGetToken(String email, String password, Role role) {
        PersonCreationDTO person = new PersonCreationDTO(
                "Test",
                "User",
                123456789L,
                email,
                password,
                "mindera",
                "Apt mindera",
                12345L,
                123456789L
        );

        given()
                .contentType(ContentType.JSON)
                .body(person)
                .when()
                .post("/api/v1/person")
                .then()
                .statusCode(201);

        PersonDTO createdPerson = personService.getPersonByEmail(email);
        personService.setPersonRole(createdPerson.getId(), role);

        String requestBody = String.format("""
                {
                    "email": "%s",
                    "password": "%s"
                }
                """, email, password);

        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getString("token");
    }

    private String generateUniqueEmail() {
        String uniquePart = UUID.randomUUID().toString().substring(0, 8);
        return "shelter-" + uniquePart + "@example.com";
    }


    @Nested
    class CrudShelter {
        @Test
        void createShelterShouldReturn201() {
            LocalDate now = LocalDate.now();
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    123456789L,
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    987654321L,
                    12L,
                    true,
                    now
            );

            ShelterDTO createdShelter =
                    given()
                            .header("Authorization", "Bearer " + userToken)
                            .contentType(ContentType.JSON)
                            .body(shelter)
                            .when()
                            .post("/api/v1/shelter")
                            .then()
                            .statusCode(201)
                            .extract().body().as(ShelterDTO.class);

            assertNotNull(createdShelter);
            assertEquals(shelter.getName(), createdShelter.getName());
        }

        @Test
        void getShelterByIdShouldReturn200() {
            LocalDate now = LocalDate.now();
            ShelterCreationDTO shelterCreationDTO = new ShelterCreationDTO(
                    "Shelter",
                    123456789L,
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    987654321L,
                    12L,
                    true,
                    now
            );

            ShelterDTO createdShelter = given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(shelterCreationDTO)
                    .when()
                    .post("/api/v1/shelter")
                    .then()
                    .statusCode(201)
                    .extract().body().as(ShelterDTO.class);

            Long shelterId = createdShelter.getId();

            ShelterDTO shelterDTO =
                    given()
                            .header("Authorization", "Bearer " + userToken)
                            .when()
                            .get("/api/v1/shelter/" + shelterId)
                            .then()
                            .statusCode(200)
                            .extract().body().as(ShelterDTO.class);

            assertNotNull(shelterDTO);
            assertEquals(shelterCreationDTO.getName(), shelterDTO.getName());
        }

        @Test
        void getAllSheltersShouldReturn200() {
            LocalDate now = LocalDate.now();
            ShelterCreationDTO shelterCreationDTO = new ShelterCreationDTO(
                    "Shelter",
                    123456789L,
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    987654321L,
                    12L,
                    true,
                    now
            );

            ShelterDTO createdShelter = given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(shelterCreationDTO)
                    .when()
                    .post("/api/v1/shelter")
                    .then()
                    .statusCode(201)
                    .extract().body().as(ShelterDTO.class);

            List<ShelterDTO> shelterDTOList =
                    given()
                            .header("Authorization", "Bearer " + userToken)
                            .when()
                            .get("/api/v1/shelter/all")
                            .then()
                            .statusCode(200)
                            .extract().body().jsonPath().getList(".", ShelterDTO.class);

            assertEquals(1, shelterDTOList.size());
        }

        @Test
        void updateShelterShouldReturn200() {
            LocalDate now = LocalDate.now();
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Original Shelter",
                    123456789L,
                    generateUniqueEmail(),
                    "Original Street",
                    "number",
                    "4400",
                    987654321L,
                    12L,
                    true,
                    now
            );

            ShelterDTO createdShelter = given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(shelter)
                    .when()
                    .post("/api/v1/shelter")
                    .then()
                    .statusCode(201)
                    .extract().body().as(ShelterDTO.class);

            createdShelter.setName("Updated Shelter");

            ShelterDTO updatedShelter = given()
                    .header("Authorization", "Bearer " + managerToken)
                    .contentType(ContentType.JSON)
                    .body(createdShelter)
                    .when()
                    .patch("/api/v1/shelter/update/" + createdShelter.getId())
                    .then()
                    .statusCode(200)
                    .extract().body().as(ShelterDTO.class);

            assertNotNull(updatedShelter);
            assertEquals("Updated Shelter", updatedShelter.getName());
        }

        @Test
        void deleteShelterShouldReturn204() {
            LocalDate now = LocalDate.now();
            ShelterCreationDTO shelterCreationDTO = new ShelterCreationDTO(
                    "Shelter",
                    123456789L,
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    987654321L,
                    12L,
                    true,
                    now
            );

            ShelterDTO shelterDTO =
                    given()
                            .header("Authorization", "Bearer " + userToken)
                            .contentType(ContentType.JSON)
                            .body(shelterCreationDTO)
                            .when()
                            .post("/api/v1/shelter")
                            .then()
                            .statusCode(201)
                            .extract().body().as(ShelterDTO.class);

            Long shelterId = shelterDTO.getId();

            given()
                    .header("Authorization", "Bearer " + managerToken)
                    .when()
                    .delete("/api/v1/shelter/delete/" + shelterId)
                    .then()
                    .statusCode(204);
        }
    }

    @Nested
    class Validation {
        @Test
        void createShelterWithNullName() {
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    null,
                    123456789L,
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    987654321L,
                    12L,
                    true,
                    LocalDate.now()
            );

            given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(shelter)
                    .when()
                    .post("/api/v1/shelter")
                    .then()
                    .statusCode(400);
        }

        @Test
        void createShelterWithNullVat() {
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    null,
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    987654321L,
                    12L,
                    true,
                    LocalDate.now()
            );

            given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(shelter)
                    .when()
                    .post("/api/v1/shelter")
                    .then()
                    .statusCode(400);
        }

        @Test
        void createShelterWithInvalidEmail() {
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    123456789L,
                    "invalid-email",
                    "Shelter Street",
                    "number",
                    "4400",
                    987654321L,
                    12L,
                    true,
                    LocalDate.now()
            );

            given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(shelter)
                    .when()
                    .post("/api/v1/shelter")
                    .then()
                    .statusCode(400);
        }

        @Test
        void createShelterWithIvalidSize() {
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    123456789L,
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    987654321L,
                    0L,
                    true,
                    LocalDate.now()
            );

            given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(shelter)
                    .when()
                    .post("/api/v1/shelter")
                    .then()
                    .statusCode(400);
        }

        @Test
        void createShelterWithAddress1Null() {
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    123456789L,
                    generateUniqueEmail(),
                    null,
                    "number",
                    "4400",
                    987654321L,
                    12L,
                    true,
                    LocalDate.now()
            );

            given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(shelter)
                    .when()
                    .post("/api/v1/shelter")
                    .then()
                    .statusCode(400);
        }

        @Test
        void createShelterWithPostalCodeNull() {
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    123456789L,
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    null,
                    987654321L,
                    12L,
                    true,
                    LocalDate.now()
            );

            given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(shelter)
                    .when()
                    .post("/api/v1/shelter")
                    .then()
                    .statusCode(400);
        }

        @Test
        void createShelterWithPhoneNull() {
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    123456789L,
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    null,
                    987654321L,
                    12L,
                    true,
                    LocalDate.now()
            );

            given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(shelter)
                    .when()
                    .post("/api/v1/shelter")
                    .then()
                    .statusCode(400);
        }

        @Test
        void createShelterWithSizeNull() {
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    123456789L,
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    null,
                    12L,
                    true,
                    LocalDate.now()
            );

            given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(shelter)
                    .when()
                    .post("/api/v1/shelter")
                    .then()
                    .statusCode(400);
        }
    }

}
*/
