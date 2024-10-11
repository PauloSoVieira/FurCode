package org.mindera.fur.code.controller.person_preferences;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.dto.person_preferences.FavoriteCreateDTO;
import org.mindera.fur.code.dto.person_preferences.FavoriteDTO;
import org.mindera.fur.code.model.Role;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.enums.pet.PetSizeEnum;
import org.mindera.fur.code.model.enums.pet.PetSpeciesEnum;
import org.mindera.fur.code.model.pet.Pet;
import org.mindera.fur.code.model.pet.PetBreed;
import org.mindera.fur.code.model.pet.PetType;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.person_preferences.FavoriteRepository;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class FavoriteControllerTest {

    private String managerToken;
    private String adminToken;
    private String userToken;
    private Long personId;
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
    private FavoriteRepository favoriteRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        createTestUsers();
        createTestData();
    }

    @AfterEach
    void tearDown() {
        favoriteRepository.deleteAll();
        //jdbcTemplate.execute("ALTER SEQUENCE favorite_id_seq RESTART WITH 1");

        personRepository.deleteAll();
        //petRepository.deleteAll();

        // Reset the auto-increment IDs
        jdbcTemplate.execute("ALTER SEQUENCE person_id_seq RESTART WITH 1");
        //jdbcTemplate.execute("ALTER SEQUENCE pet_id_seq RESTART WITH 1");
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

        if (role == Role.USER) {
            personId = createdPerson.getId();
        }

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

    private void createTestData() {
        // Create and save a shelter
        Shelter shelter = new Shelter();
        shelter.setName("Test Shelter");
        shelter.setVat("123456789");
        shelter.setEmail("shelter@example.com");
        shelter.setAddress1("123 Shelter St");
        shelter.setAddress2("Suite 100");
        shelter.setPostalCode("12345");
        shelter.setPhone("5551234567");
        shelter.setSize("50");
        shelter.setIsActive(true);
        shelter.setCreationDate(LocalDate.now());
        shelterRepository.save(shelter);

        // Create and save a breed
        PetBreed breed = new PetBreed();
        breed.setExternalApiId("breed-123");
        breed.setName("Golden Retriever");
        breed.setDescription("Friendly and active");
        petBreedRepository.save(breed);

        // Create and save a pet type
        PetType petType = new PetType();
        petType.setSpecies(PetSpeciesEnum.DOG);
        petType.setBreed(breed);
        petTypeRepository.save(petType);

        // Create and save a pet
        Pet pet = new Pet();
        pet.setName("Buddy");
        pet.setPetType(petType);
        pet.setShelter(shelter);
        pet.setIsAdopted(false);
        pet.setIsVaccinated(true);
        pet.setSize(PetSizeEnum.MEDIUM);
        pet.setWeight(20.0);
        pet.setColor("Golden");
        pet.setAge(2);
        pet.setObservations("Healthy and friendly");
        petRepository.save(pet);

        petId = pet.getId();
    }

    @Test
    void addFavorite_shouldReturn201() {
        FavoriteCreateDTO createDTO = new FavoriteCreateDTO();
        createDTO.setPersonId(personId);
        createDTO.setPetId(petId);

        FavoriteDTO createdFavorite = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + userToken)
                .body(createDTO)
                .when()
                .post("/api/v1/favorite/add")
                .then()
                .statusCode(201)
                .extract().as(FavoriteDTO.class);

        assertNotNull(createdFavorite);
        assertEquals(personId, createdFavorite.getPersonId());
        assertEquals(petId, createdFavorite.getPetId());
        assertNotNull(createdFavorite.getFavoriteAt());
    }

    @Test
    void getFavoritesByPerson_shouldReturnFavorites() {
        addFavorite_shouldReturn201();

        List<FavoriteDTO> favorites = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + userToken)
                .when()
                .get("/api/v1/favorite/person/" + personId)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", FavoriteDTO.class);

        assertFalse(favorites.isEmpty());
        assertEquals(personId, favorites.get(0).getPersonId());
    }

    @Test
    void isFavorite_shouldReturnTrue() {
        addFavorite_shouldReturn201();

        Boolean isFavorite = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + userToken)
                .when()
                .get(String.format("/api/v1/favorite/%d/%d", personId, petId))
                .then()
                .statusCode(200)
                .extract().as(Boolean.class);

        assertTrue(isFavorite);
    }

    @Test
    void removeFavorite_shouldReturn204() {
        addFavorite_shouldReturn201();

        given()
                .header("Authorization", "Bearer " + userToken)
                .when()
                .delete(String.format("/api/v1/favorite/delete/%d/%d", personId, petId))
                .then()
                .statusCode(204);

        Boolean isFavorite = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + userToken)
                .when()
                .get(String.format("/api/v1/favorite/%d/%d", personId, petId))
                .then()
                .statusCode(200)
                .extract().as(Boolean.class);

        assertFalse(isFavorite);
    }

    @Test
    void addFavorite_forAdoptedPet_shouldReturn409() {
        Pet pet = petRepository.findById(petId).orElseThrow();
        pet.setIsAdopted(true);
        petRepository.save(pet);

        FavoriteCreateDTO createDTO = new FavoriteCreateDTO();
        createDTO.setPersonId(personId);
        createDTO.setPetId(petId);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + userToken)
                .body(createDTO)
                .when()
                .post("/api/v1/favorite/add")
                .then()
                .statusCode(409)
                .extract().body().jsonPath().param("message", containsString("Only available pets can be favorite"));
    }

    @Test
    void addFavorite_alreadyExists_shouldReturn409() {
        addFavorite_shouldReturn201();

        FavoriteCreateDTO createDTO = new FavoriteCreateDTO();
        createDTO.setPersonId(personId);
        createDTO.setPetId(petId);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + userToken)
                .body(createDTO)
                .when()
                .post("/api/v1/favorite/add")
                .then()
                .statusCode(409)
                .extract().body().jsonPath().param("message", containsString("Pet is already in favorites"));
    }

    @Test
    void removeFavorite_notExists_shouldReturn404() {
        given()
                .header("Authorization", "Bearer " + userToken)
                .when()
                .delete(String.format("/api/v1/favorite/delete/%d/%d", personId, petId))
                .then()
                .statusCode(404)
                .extract().body().jsonPath().param("message", containsString("Pet is not in favorites"));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidFavoriteData")
    void addFavorite_withInvalidData_shouldReturn400(String favoriteJson, String expectedErrorMessage) {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + userToken)
                .body(favoriteJson)
                .when()
                .post("/api/v1/favorite/add")
                .then()
                .statusCode(400)
                .extract().body().jsonPath().param("message", containsString(expectedErrorMessage));
    }

    private static Stream<Arguments> provideInvalidFavoriteData() {
        return Stream.of(
                // personId is null
                Arguments.of(
                        """
                        {
                            "personId": null,
                            "petId": 1
                        }
                        """,
                        "Person ID must be provided"
                ),
                // personId is negative
                Arguments.of(
                        """
                        {
                            "personId": -1,
                            "petId": 1
                        }
                        """,
                        "Person ID must be greater than 0"
                ),
                // petId is null
                Arguments.of(
                        """
                        {
                            "personId": 1,
                            "petId": null
                        }
                        """,
                        "Pet ID must be provided"
                ),
                // petId is negative
                Arguments.of(
                        """
                        {
                            "personId": 1,
                            "petId": -1
                        }
                        """,
                        "Pet ID must be greater than 0"
                ),
                // personId is a string
                Arguments.of(
                        """
                        {
                            "personId": "one",
                            "petId": 1
                        }
                        """,
                        "Person ID must be an integer"
                ),
                // petId is a string
                Arguments.of(
                        """
                        {
                            "personId": 1,
                            "petId": "one"
                        }
                        """,
                        "Pet ID must be an integer"
                ),
                // personId is a decimal
                Arguments.of(
                        """
                        {
                            "personId": 1,5,
                            "petId": 1
                        }
                        """,
                        "Person ID must be an integer"
                ),
                // petId is a decimal
                Arguments.of(
                        """
                        {
                            "personId": 1,
                            "petId": 1,5
                        }
                        """,
                        "Pet ID must be an integer"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideRolesForAddFavorite")
    void addFavorite_withDifferentRoles_shouldReturnExpectedStatus(String role, int expectedStatus) {
        String token = getTokenForRole(role);

        FavoriteCreateDTO createDTO = new FavoriteCreateDTO();
        createDTO.setPersonId(personId);
        createDTO.setPetId(petId);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(createDTO)
                .when()
                .post("/api/v1/favorite/add")
                .then()
                .statusCode(expectedStatus);
    }

    private static Stream<Arguments> provideRolesForAddFavorite() {
        return Stream.of(
                Arguments.of("USER", 201),
                Arguments.of("MANAGER", 201),
                Arguments.of("ADMIN", 201)
        );
    }

    private String getTokenForRole(String role) {
        return switch (role) {
            case "USER" -> userToken;
            case "MANAGER" -> managerToken;
            case "ADMIN" -> adminToken;
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
    }
}












