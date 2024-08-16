package org.mindera.fur.code;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.mindera.fur.code.repository.ShelterPersonRolesRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class PersonControllerIntegrationTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "correctpassword";

    @LocalServerPort
    private Integer port;

    @Autowired
    private PersonService personService;
    private String adminToken;
    private String managerToken;
    private String userToken;
    private ShelterPersonRolesRepository shelterPersonRolesRepository;
    private ShelterRepository shelterRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        createTestUsers();
    }

    @AfterEach
    void tearDown() {
        shelterPersonRolesRepository.deleteAll();
        shelterRepository.deleteAll();

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

    @Nested
    class CrudPerson {
        @Test
        void createPersonShouldReturn201() {
            PersonCreationDTO person = new PersonCreationDTO(
                    "jojo",
                    "da wish",
                    123456789L,
                    "jojo@example.com",
                    "password",
                    "mindera",
                    "Apt mindera",
                    12345L,
                    123456789L
            );

            PersonDTO personDTO =
                    given()
                            .contentType(ContentType.JSON)
                            .body(person)
                            .when()
                            .post("/api/v1/person")
                            .then()
                            .statusCode(201)
                            .extract().body().as(PersonDTO.class);

            assertEquals(person.getFirstName(), "jojo");
        }


        @Test
        void getPersonByIdShouldReturn200() {
            String uniqueEmail = "jojo.wish." + UUID.randomUUID().toString() + "@example.com";

            PersonCreationDTO personCreationDTO = new PersonCreationDTO(
                    "jojo",
                    "da wish",
                    123456789L,
                    "jojo@example.com",
                    "password",
                    "mindera",
                    "Apt mindera",
                    12345L,
                    123456789L
            );

            PersonDTO createdPerson = given()
                    .contentType(ContentType.JSON)
                    .body(personCreationDTO)
                    .when()
                    .post("/api/v1/person")
                    .then()
                    .statusCode(201)
                    .extract().body().as(PersonDTO.class);

            Long personId = createdPerson.getId();

            PersonDTO retrievedPerson = given()
                    .header("Authorization", "Bearer " + adminToken)
                    .when()
                    .get("/api/v1/person/" + personId)
                    .then()
                    .statusCode(200)
                    .extract().body().as(PersonDTO.class);

            assertEquals(createdPerson.getFirstName(), retrievedPerson.getFirstName());
            assertEquals(createdPerson.getLastName(), retrievedPerson.getLastName());
        }

        @Test
        void getAllPersonsShouldReturn200() {
            List<PersonDTO> personDTOList =
                    given()
                            .header("Authorization", "Bearer " + adminToken)
                            .when()
                            .get("/api/v1/person/all")
                            .then()
                            .statusCode(200)
                            .extract().body().jsonPath().getList(".", PersonDTO.class);

            assert (personDTOList.size() >= 3);
        }

        @Test
        void updatePersonShouldReturn200() {
            PersonCreationDTO personCreationDTO = new PersonCreationDTO(
                    "jojo",
                    "da wish",
                    123456789L,
                    "jojo@example.com",
                    "password",
                    "mindera",
                    "Apt mindera",
                    12345L,
                    123456789L
            );

            PersonDTO createdPerson = given()
                    .contentType(ContentType.JSON)
                    .body(personCreationDTO)
                    .when()
                    .post("/api/v1/person")
                    .then()
                    .statusCode(201)
                    .extract().body().as(PersonDTO.class);

            Long personId = createdPerson.getId();

            createdPerson.setEmail("johjo@gmail.com");

            PersonDTO updatedPerson = given()
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(ContentType.JSON)
                    .body(createdPerson)
                    .when()
                    .patch("/api/v1/person/update/" + personId)
                    .then()
                    .statusCode(200)
                    .extract().body().as(PersonDTO.class);

            assertEquals("johjo@gmail.com", updatedPerson.getEmail());
        }

        @Test
        void deletePersonShouldReturn204() {
            PersonCreationDTO personCreationDTO = new PersonCreationDTO(
                    "jojo",
                    "da wish",
                    123456789L,
                    "jojo@example.com",
                    "password",
                    "mindera",
                    "Apt mindera",
                    12345L,
                    123456789L
            );

            PersonDTO personDTO =
                    given()
                            .contentType(ContentType.JSON)
                            .body(personCreationDTO)
                            .when()
                            .post("/api/v1/person")
                            .then()
                            .statusCode(201)
                            .extract().body().as(PersonDTO.class);

            given()
                    .header("Authorization", "Bearer " + managerToken)
                    .when()
                    .delete("/api/v1/person/delete/" + personDTO.getId())
                    .then()
                    .statusCode(204);
        }

        @Test
        void createShelterShouldReturn201() {
            String uniqueEmail = "jojo" + UUID.randomUUID().toString() + "@example.com";
            PersonCreationDTO personCreationDTO = new PersonCreationDTO(
                    "jojo",
                    "da wish",
                    123456789L,
                    uniqueEmail,
                    "password",
                    "mindera",
                    "Apt mindera",
                    12345L,
                    123456789L
            );

            Long personId = given()
                    .contentType(ContentType.JSON)
                    .body(personCreationDTO)
                    .when()
                    .post("/api/v1/person")
                    .then()
                    .statusCode(201)
                    .extract().body().jsonPath().getLong("id");

            Date date = new Date();
            ShelterCreationDTO shelterCreationDTO = new ShelterCreationDTO(
                    "Shelter",
                    123456789L,
                    "shelter@shelter.com",
                    "Shelter Street",
                    "number",
                    "4400",
                    987654321L,
                    12L,
                    true,
                    date
            );

            try {
                System.out.println("Request Body: " + new ObjectMapper().writeValueAsString(shelterCreationDTO));
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to serialize request body", e);
            }

            ShelterDTO shelterDTO =
                    given()
                            .header("Authorization", "Bearer " + userToken)
                            .contentType(ContentType.JSON)
                            .body(shelterCreationDTO)
                            .when()
                            .post("/api/v1/person/" + personId + "/create-shelter")
                            .then()
                            .statusCode(201)
                            .extract().body().as(ShelterDTO.class);

            assertNotNull(shelterDTO, "ShelterDTO should not be null");
            //assertNotNull(shelterDTO.getName(), "Shelter name should not be null");
            assertEquals("Shelter", shelterDTO.getName());
            assertEquals(123456789L, shelterDTO.getVat());
            assertEquals("shelter@shelter.com", shelterDTO.getEmail());


        }

        @Nested
        class Validation {
            @Test
            void createPersonWithNullName() {
                PersonCreationDTO person = new PersonCreationDTO(
                        null,
                        "da wish",
                        123456789L,
                        "jojo@example.com",
                        "password",
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
                        .statusCode(400);
            }

        }
    }
}