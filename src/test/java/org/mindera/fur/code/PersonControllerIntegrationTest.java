package org.mindera.fur.code;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class PersonControllerIntegrationTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        personService.deleteAllPersons();
    }

    @Nested
    class crudPerson {
        @Test
        void createPersonShouldReturn201() {
            PersonCreationDTO person = new PersonCreationDTO(
                    "John",
                    "Doe",
                    123456789L,
                    "john.doe@example.com",
                    "password",
                    "123 Main Street",
                    "Apt 1",
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

            Assertions.assertEquals(person.getFirstName(), "John");


        }

        @Test
        void getPersonByIdShouldReturn200() {
            PersonCreationDTO personCreationDTO = new PersonCreationDTO(
                    "John",
                    "Doe",
                    123456789L,
                    "john.doe@example.com",
                    "password",
                    "123 Main Street",
                    "Apt 1",
                    12345L,
                    123456789L
            );

            String personId =
                    given()
                            .contentType(ContentType.JSON)
                            .body(personCreationDTO)
                            .when()
                            .post("/api/v1/person")
                            .then()
                            .statusCode(201).extract().body().jsonPath().getString("id");

            PersonDTO findPersonDTO =
                    given()
                            .when()
                            .get("/api/v1/person/" + personId)
                            .then()
                            .statusCode(200).extract().body().as(PersonDTO.class);
        }

        @Test
        void addPersonToShelterShouldReturn201() {
            PersonCreationDTO personCreationDTO = new PersonCreationDTO(
                    "John",
                    "Doe",
                    123456789L,
                    "john.doe@example.com",
                    "password",
                    "123 Main Street",
                    "Apt 1",
                    12345L,
                    123456789L
            );

            String personId =
                    given()
                            .contentType(ContentType.JSON)
                            .body(personCreationDTO)
                            .when()
                            .post("/api/v1/person")
                            .then()
                            .statusCode(201).extract().body().jsonPath().getString("id");

            ShelterCreationDTO shelterCreationDTO = new ShelterCreationDTO(
                    "Shelter"
            );

            String shelterId =
                    given()
                            .contentType(ContentType.JSON)
                            .body(shelterCreationDTO)
                            .when()
                            .post("/api/v1/shelter")
                            .then()
                            .statusCode(201).extract().jsonPath().getString("id");

        }

        @Test
        void getAllPersonsShouldReturn200() {
            PersonCreationDTO personCreationDTO = new PersonCreationDTO(

                    "John",
                    "Doe",
                    123456789L,
                    "john.doe@example.com",
                    "password",
                    "123 Main Street",
                    "Apt 1",
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
                            .statusCode(201).extract().body().as(PersonDTO.class);

            List<PersonDTO> personDTOList =
                    given()
                            .when()
                            .get("/api/v1/person/all")
                            .then()
                            .statusCode(200).extract().body().jsonPath().getList(".", PersonDTO.class);

            Assertions.assertEquals(1, personDTOList.size());
        }

        @Test
        void updatePersonShouldReturn200() {
            PersonCreationDTO personCreationDTO = new PersonCreationDTO(
                    "John",
                    "Doe",
                    123456789L,
                    "john.doe@example.com",
                    "password",
                    "123 Main Street",
                    "Apt 1",
                    12345L,
                    123456789L
            );

            String personId =
                    given()
                            .contentType(ContentType.JSON)
                            .body(personCreationDTO)
                            .when()
                            .post("/api/v1/person")
                            .then()
                            .statusCode(201).extract().body().jsonPath().getString("id");


            personCreationDTO.setEmail("johndoe@gmail.com");

            PersonDTO updatedPersonDTO =
                    given()
                            .contentType(ContentType.JSON)
                            .body(personCreationDTO)
                            .when()
                            .post("/api/v1/person")
                            .then()
                            .statusCode(201).extract().body().as(PersonDTO.class);

            Assertions.assertEquals("johndoe@gmail.com", updatedPersonDTO.getEmail());
        }

        @Test
        void deletePersonShouldReturn204() {
            PersonCreationDTO personCreationDTO = new PersonCreationDTO(
                    "John",
                    "Doe",
                    123456789L,
                    "john.doe@example.com",
                    "password",
                    "123 Main Street",
                    "Apt 1",
                    12345L,
                    123456789L
            );

            String personId =
                    given()
                            .contentType(ContentType.JSON)
                            .body(personCreationDTO)
                            .when()
                            .post("/api/v1/person")
                            .then()
                            .statusCode(201).extract().body().jsonPath().getString("id");

            given()
                    .when()
                    .delete("/api/v1/person/delete/" + personId)
                    .then()
                    .statusCode(204);

        }

        @Test
        void createShelterShouldReturn201() {
            PersonCreationDTO personCreationDTO = new PersonCreationDTO(
                    "John",
                    "Doe",
                    123456789L,
                    "john.doe@example.com",
                    "password",
                    "123 Main Street",
                    "Apt 1",
                    12345L,
                    123456789L
            );

            String personId =
                    given()
                            .contentType(ContentType.JSON)
                            .body(personCreationDTO)
                            .when()
                            .post("/api/v1/person")
                            .then()
                            .statusCode(201).extract().body().jsonPath().getString("id");

            ShelterCreationDTO shelterCreationDTO = new ShelterCreationDTO(
                    "Shelter"
            );

        }
    }

    @Nested
    class validation {

        //TODO ALTERAR O TESTE  ESTA 404 MANUALMENTE NO SERVIÇO
        @Test
        void createPersonWithNullName() {
            PersonCreationDTO personCreationDTO = new PersonCreationDTO(
                    null, "Doe", 123456789L, "john.doe@example.com", "password",
                    "123 Main Street", "Apt 1", 12345L, 123456789L
            );
            given()
                    .contentType(ContentType.JSON)
                    .body(personCreationDTO)
                    .when()
                    .post("/api/v1/person")
                    .then()
                    .statusCode(400);
        }

    }
}



