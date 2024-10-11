package org.mindera.fur.code.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.model.Role;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ShelterControllerTest {

    // A valid Shelter JSON to use globally across multiple tests
    private static final String VALID_SHELTER_JSON = """
        {
          "name": "Happy Tails Shelter",
          "vat": "123456789",
          "email": "happytails@example.com",
          "address1": "1234 Bark Street",
          "address2": "Suite 100",
          "postalCode": "12345",
          "phone": "5551234567",
          "size": "50",
          "isActive": true,
          "creationDate": "2023-01-01",
          "description": "A shelter for happy tails",
          "facebookUrl": "http://facebook.com/happytails",
          "instagramUrl": "http://instagram.com/happytails",
          "webPageUrl": "http://happytails.com"
        }
    """;

    // A valid Shelter Update JSON
    private static final String VALID_SHELTER_UPDATE_JSON = """
        {
          "name": "Happy Paws Shelter",
          "email": "happypaws@example.com",
          "phone": "5557654321",
          "description": "Updated description for the shelter",
          "isActive": false
        }
    """;

    @LocalServerPort
    private Integer port;

    @Autowired
    private PersonService personService;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String adminToken;
    private String managerToken;
    private String userToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        createTestUsers();
//        ShelterDTO shelterDTO = createTestShelter();
    }

    @AfterEach
    void tearDown() {
        shelterRepository.deleteAll();
        personRepository.deleteAll();

        jdbcTemplate.execute("ALTER SEQUENCE person_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE shelter_id_seq RESTART WITH 1");
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

    private ShelterDTO createTestShelter() {
        LocalDate now = LocalDate.now();
        ShelterCreationDTO shelterCreationDTO = new ShelterCreationDTO(
                "Shelter",
                "123456789",
                "shelter@shelter.com",
                "Shelter Street",
                "Shelter Street 2",
                "4400",
                "987654321",
                "12",
                true,
                now,
                "This is a description",
                "era isso",
                "que ia fazer",
                "mas obrigado."
        );

        return given()
                .header("Authorization", "Bearer " + userToken)
                .contentType(ContentType.JSON)
                .body(shelterCreationDTO)
                .when()
                .post("/api/v1/shelter")
                .then()
                .statusCode(201)
                .extract().body().as(ShelterDTO.class);
    }

    private Long createShelterAndGetId() {
        Integer id = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + managerToken)
                .body(VALID_SHELTER_JSON)
                .when()
                .post("/api/v1/shelter")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
        return id.longValue();
    }

    @Nested
    class CrudShelter {
        @Test
        void createShelterShouldReturn201() {
            LocalDate now = LocalDate.now();
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    "123456789",
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    "987654321",
                    "12",
                    true,
                    now,
                    "This is a description",
                    "era isso",
                    "que ia fazer",
                    "mas obrigado."
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
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    "123456789",
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    "987654321",
                    "12",
                    true,
                    now,
                    "This is a description",
                    "era isso",
                    "que ia fazer",
                    "mas obrigado."
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
            assertEquals(shelter.getName(), shelterDTO.getName());
        }

        @Test
        void getAllSheltersShouldReturn200() {
            LocalDate now = LocalDate.now();
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    "123456789",
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    "987654321",
                    "12",
                    true,
                    now,
                    "This is a description",
                    "era isso",
                    "que ia fazer",
                    "mas obrigado."
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
                    "Shelter",
                    "123456789",
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    "987654321",
                    "12",
                    true,
                    now,
                    "This is a description",
                    "era isso",
                    "que ia fazer",
                    "mas obrigado."
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
                    .put("/api/v1/shelter/update/" + createdShelter.getId())
                    .then()
                    .statusCode(200)
                    .extract().body().as(ShelterDTO.class);

            assertNotNull(updatedShelter);
            assertEquals("Updated Shelter", updatedShelter.getName());
        }

        @Test
        void deleteShelterShouldReturn204() {
            LocalDate now = LocalDate.now();
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    "123456789",
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    "987654321",
                    "12",
                    true,
                    now,
                    "This is a description",
                    "era isso",
                    "que ia fazer",
                    "mas obrigado."
            );

            ShelterDTO shelterDTO =
                    given()
                            .header("Authorization", "Bearer " + userToken)
                            .contentType(ContentType.JSON)
                            .body(shelter)
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
            LocalDate now = LocalDate.now();
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    null,
                    "123456789",
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    "987654321",
                    "12",
                    true,
                    now,
                    "This is a description",
                    "era isso",
                    "que ia fazer",
                    "mas obrigado."
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
            LocalDate now = LocalDate.now();
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    null,
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    "987654321",
                    "12",
                    true,
                    now,
                    "This is a description",
                    "era isso",
                    "que ia fazer",
                    "mas obrigado."
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
            LocalDate now = LocalDate.now();
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    "123456789",
                    "generateUniqueEmail()",
                    "Shelter Street",
                    "number",
                    "4400",
                    "987654321",
                    "12",
                    true,
                    now,
                    "This is a description",
                    "era isso",
                    "que ia fazer",
                    "mas obrigado."
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
            LocalDate now = LocalDate.now();
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    "123456789",
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    "987654321",
                    "1Yep",
                    true,
                    now,
                    "This is a description",
                    "era isso",
                    "que ia fazer",
                    "mas obrigado."
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
            LocalDate now = LocalDate.now();
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    "123456789",
                    generateUniqueEmail(),
                    null,
                    "number",
                    "4400",
                    "987654321",
                    "12",
                    true,
                    now,
                    "This is a description",
                    "era isso",
                    "que ia fazer",
                    "mas obrigado."
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
            LocalDate now = LocalDate.now();
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    "123456789",
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    null,
                    "987654321",
                    "12",
                    true,
                    now,
                    "This is a description",
                    "era isso",
                    "que ia fazer",
                    "mas obrigado."
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
            LocalDate now = LocalDate.now();
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    "123456789",
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    null,
                    "12",
                    true,
                    now,
                    "This is a description",
                    "era isso",
                    "que ia fazer",
                    "mas obrigado."
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
            LocalDate now = LocalDate.now();
            ShelterCreationDTO shelter = new ShelterCreationDTO(
                    "Shelter",
                    "123456789",
                    generateUniqueEmail(),
                    "Shelter Street",
                    "number",
                    "4400",
                    "987654321",
                    null,
                    true,
                    now,
                    "This is a description",
                    "era isso",
                    "que ia fazer",
                    "mas obrigado."
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

    @ParameterizedTest
    @MethodSource("provideEndpointsForRolePermissionTests")
    void user_withDifferentRoles_shouldReturnExpectedStatus(String role, String method, String urlTemplate, int expectedStatus) {
        Long shelterId = createShelterAndGetId();

        String token = switch (role) {
            case "ADMIN" -> adminToken;
            case "MANAGER" -> managerToken;
            case "USER" -> userToken;
            default -> throw new IllegalArgumentException("Unsupported role: " + role);
        };

        RequestSpecification request = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);

        String url = urlTemplate.contains("%d") ? String.format(urlTemplate, shelterId) : urlTemplate;

        String requestBody = getRequestBody(url);

        switch (method) {
            case "POST":
                request.body(requestBody)
                        .when()
                        .post(url)
                        .then()
                        .statusCode(expectedStatus);
                break;
            case "GET":
                request.when()
                        .get(url)
                        .then()
                        .statusCode(expectedStatus);
                break;
            case "PUT":
                request.body(requestBody)
                        .when()
                        .put(url)
                        .then()
                        .statusCode(expectedStatus);
                break;
            case "DELETE":
                request.when()
                        .delete(url)
                        .then()
                        .statusCode(expectedStatus);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }

    @NotNull
    private String getRequestBody(String url) {
        String requestBody;
        if (url.equals("/api/v1/shelter")) {
            requestBody = VALID_SHELTER_JSON; // Valid shelter creation JSON
        } else if (url.contains("/update")) {
            requestBody = VALID_SHELTER_UPDATE_JSON; // Valid shelter update JSON
        } else {
            requestBody = ""; // For endpoints that don’t need a body
        }
        return requestBody;
    }

    private static Stream<Arguments> provideEndpointsForRolePermissionTests() {
        return Stream.of(
                // Test POST endpoints
                Arguments.of("MANAGER", "POST", "/api/v1/shelter", 201),
                Arguments.of("ADMIN", "POST", "/api/v1/shelter", 201),
                Arguments.of("USER", "POST", "/api/v1/shelter", 201),

                // Test GET endpoints
                Arguments.of("MANAGER", "GET", "/api/v1/shelter/all", 200),
                Arguments.of("ADMIN", "GET", "/api/v1/shelter/all", 200),
                Arguments.of("USER", "GET", "/api/v1/shelter/all", 200),

                Arguments.of("MANAGER", "GET", "/api/v1/shelter/%d", 200),
                Arguments.of("ADMIN", "GET", "/api/v1/shelter/%d", 200),
                Arguments.of("USER", "GET", "/api/v1/shelter/%d", 200),

                Arguments.of("MANAGER", "GET", "/api/v1/shelter/%d/get-all-donations", 200),
                Arguments.of("ADMIN", "GET", "/api/v1/shelter/%d/get-all-donations", 200),
                Arguments.of("USER", "GET", "/api/v1/shelter/%d/get-all-donations", 403),

                Arguments.of("MANAGER", "GET", "/api/v1/shelter/%d/allPets", 200),
                Arguments.of("ADMIN", "GET", "/api/v1/shelter/%d/allPets", 200),
                Arguments.of("USER", "GET", "/api/v1/shelter/%d/allPets", 200),

                // Test PUT endpoints
                Arguments.of("MANAGER", "PUT", "/api/v1/shelter/update/%d", 200),
                Arguments.of("ADMIN", "PUT", "/api/v1/shelter/update/%d", 403),
                Arguments.of("USER", "PUT", "/api/v1/shelter/update/%d", 403),

                // Test DELETE endpoints
                Arguments.of("MANAGER", "DELETE", "/api/v1/shelter/delete/%d", 204),
                Arguments.of("ADMIN", "DELETE", "/api/v1/shelter/delete/%d", 403),
                Arguments.of("USER", "DELETE", "/api/v1/shelter/delete/%d", 403)
        );
    }
}

