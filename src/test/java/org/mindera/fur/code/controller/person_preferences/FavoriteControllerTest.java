package org.mindera.fur.code.controller.person_preferences;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.model.Role;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.enums.pet.PetSpeciesEnum;
import org.mindera.fur.code.model.pet.PetBreed;
import org.mindera.fur.code.model.pet.PetType;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.pet.PetBreedRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.mindera.fur.code.repository.pet.PetTypeRepository;
import org.mindera.fur.code.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class FavoriteControllerTest {
    private String managerToken;
    private String adminToken;
    private String userToken;
    private Long petId;

    @LocalServerPort
    private int port;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private PetBreedRepository petBreedRepository;

    @Autowired
    private PetTypeRepository petTypeRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        createTestUsers();

        // Create and save a shelter
        LocalDate now = LocalDate.now();
        Shelter shelter = new Shelter();
        shelter.setName("Test shelter");
        shelter.setVat(123456789L);
        shelter.setEmail("shelter@shelter.com");
        shelter.setAddress1("Shelter Street");
        shelter.setAddress2("number");
        shelter.setPostalCode("4400");
        shelter.setPhone(987654321L);
        shelter.setSize(234L);
        shelter.setIsActive(true);
        shelter.setCreationDate(now);
        shelterRepository.save(shelter);

        // Create and save a breed
        PetBreed breed = new PetBreed();
        breed.setExternalApiId("23yrt-tht-r46");
        breed.setName("Hokkaido");
        breed.setDescription("A strong and independent dog breed.");
        petBreedRepository.save(breed);

        // Create and save a pet type with the breed
        PetType petType = new PetType();
        petType.setSpecies(PetSpeciesEnum.DOG);
        petType.setBreed(breed);
        petTypeRepository.save(petType);

        // Create the pet once and store its ID for use in all tests
        petId = createPetAndGetId();
    }

    @AfterEach
    void tearDown() {
        petRepository.deleteAll();
        personRepository.deleteAll();

        // Reset the auto-increment IDs
        jdbcTemplate.execute("ALTER SEQUENCE pet_id_seq RESTART WITH 1");
    }

    private void createTestUsers() {
        managerToken = createUserAndGetToken("manager@example.com", "managerpass", Role.MANAGER);
        adminToken = createUserAndGetToken("admin@example.com", "adminpass", Role.ADMIN);
        userToken = createUserAndGetToken("user@example.com", "userpass", Role.USER);
    }

    private String createUserAndGetToken(String email, String password, Role role) {
        // Create the user
        PersonCreationDTO person = new PersonCreationDTO(
                "Test",
                "User",
                123456789L,
                email,
                password,
                "Street 1",
                "Apt 2",
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

        // Assign the role to the user
        PersonDTO createdPerson = personService.getPersonByEmail(email);
        personService.setPersonRole(createdPerson.getId(), role);

        // Log in and get the token
        String loginRequest = String.format("""
                    {
                        "email": "%s",
                        "password": "%s"
                    }
                """, email, password);

        return given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getString("token");
    }

    @Test
    void testConnection() {
        given()
                .when()
                .get("/api/v1/pet/all")
                .then()
                .statusCode(200);
    }

    // A validPetJson to use globally across multiple tests
    private static final String VALID_PET_JSON = """
                {
                  "name": "Max",
                  "petTypeId": 1,
                  "shelterId": 1,
                  "isAdopted": false,
                  "isVaccinated": true,
                  "size": "LARGE",
                  "weight": 25.5,
                  "color": "Brown",
                  "age": 3,
                  "observations": "Healthy and active"
                }
            """;

    private Long createPetAndGetId() {
        Integer idAsInteger = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken) // Admin token
                .body(VALID_PET_JSON)
                .when()
                .post("/api/v1/pet")
                .then()
                .statusCode(201)
                .extract()
                .path("id"); // Extract and return the pet's ID

        return idAsInteger.longValue();
    }




}












