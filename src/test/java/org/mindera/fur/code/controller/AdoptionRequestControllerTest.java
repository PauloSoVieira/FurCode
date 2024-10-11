package org.mindera.fur.code.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.dto.shelter.ShelterDTO;
import org.mindera.fur.code.model.Role;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.State;
import org.mindera.fur.code.model.enums.pet.PetSizeEnum;
import org.mindera.fur.code.model.enums.pet.PetSpeciesEnum;
import org.mindera.fur.code.model.pet.Pet;
import org.mindera.fur.code.model.pet.PetBreed;
import org.mindera.fur.code.model.pet.PetType;
import org.mindera.fur.code.repository.AdoptionRequestRepository;
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
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class AdoptionRequestControllerTest {

    private static final String VALID_ADOPTION_REQUEST_JSON = """
        {
          "shelterId": 1,
          "personId": 1,
          "petId": 1,
          "state": "SENT"
        }
    """;

    private String userToken;
    private String adminToken;
    private String managerToken;

    @LocalServerPort
    private Integer port;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PetBreedRepository petBreedRepository;

    @Autowired
    private PetTypeRepository petTypeRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private AdoptionRequestRepository adoptionRequestRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        userToken = createUserAndGetToken("user@example.com", "userpass", Role.USER);
        adminToken = createUserAndGetToken("admin@example.com", "adminpass", Role.ADMIN);
        managerToken = createUserAndGetToken("manager@example.com", "managerpass", Role.MANAGER);

        ShelterDTO shelterDTO = createTestShelter();
        createTestPet(shelterDTO.getId());
    }

    @AfterEach
    void tearDown() {
        adoptionRequestRepository.deleteAll();
        shelterRepository.deleteAll();
        personRepository.deleteAll();
        petRepository.deleteAll();

        // Reset the auto-increment IDs
        jdbcTemplate.execute("ALTER SEQUENCE adoption_request_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE shelter_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE person_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE pet_id_seq RESTART WITH 1");
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

    private void createTestPet(Long shelterId) {
        Shelter shelter = shelterRepository.findById(shelterId)
                .orElseThrow(() -> new RuntimeException("Shelter not found"));

        PetBreed breed = new PetBreed();
        breed.setExternalApiId("23yrt-tht-r46");
        breed.setName("Hokkaido");
        breed.setDescription("A strong and independent dog breed.");
        petBreedRepository.save(breed);

        PetType petType = new PetType();
        petType.setSpecies(PetSpeciesEnum.DOG);
        petType.setBreed(breed);
        petTypeRepository.save(petType);

        Pet pet = new Pet();
        pet.setName("Bobby");
        pet.setPetType(petType);
        pet.setShelter(shelter);
        pet.setIsAdopted(false);
        pet.setIsVaccinated(true);
        pet.setSize(PetSizeEnum.SMALL);
        pet.setWeight(5.5);
        pet.setColor("Brown");
        pet.setAge(3);
        pet.setObservations("Friendly and playful");

        pet.setShelter(shelter);
        petRepository.save(pet);
    }

    private Long createAdoptionRequestAndGetId() {
        Integer id = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(VALID_ADOPTION_REQUEST_JSON)
                .when()
                .post("/api/v1/adoption-request")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
        return id.longValue();
    }

    @Test
    void testConnection() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .get("/api/v1/adoption-request/all")
                .then()
                .statusCode(200);
    }

    @Test
    void createAdoptionRequestShouldReturn201() {
        // A validPetJson to use globally across multiple tests
        String VALID_ADOPTION_REQUEST_JSON = """
                    {
                      "shelterId": 1,
                      "personId": 1,
                      "petId": 1,
                      "state": "SENT"
                    }
                """;

        AdoptionRequestDTO createdRequest = given()
                .header("Authorization", "Bearer " + managerToken)
                .contentType(ContentType.JSON)
                .body(VALID_ADOPTION_REQUEST_JSON)
                .when()
                .post("/api/v1/adoption-request")
                .then()
                .statusCode(201)
                .extract().body().as(AdoptionRequestDTO.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(VALID_ADOPTION_REQUEST_JSON);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(createdRequest, "Created request should not be null");
        assertNotNull(createdRequest.getId(), "Created request ID should not be null");
        assertEquals(jsonNode.get("shelterId").asInt(), createdRequest.getShelterId(), "Shelter IDs should match");
        assertEquals(jsonNode.get("personId").asInt(), createdRequest.getPersonId(), "Person IDs should match");
        assertEquals(jsonNode.get("petId").asInt(), createdRequest.getPetId(), "Pet IDs should match");
    }

    @Test
    void getAdoptionRequestByIdShouldReturn200() {
        AdoptionRequestCreationDTO request = new AdoptionRequestCreationDTO(1L, 1L, 1L, State.SENT);

        AdoptionRequestDTO createdRequest = given()
                .header("Authorization", "Bearer " + managerToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/adoption-request")
                .then()
                .statusCode(201)
                .extract().body().as(AdoptionRequestDTO.class);

        AdoptionRequestDTO retrievedRequest =
                given()
                        .header("Authorization", "Bearer " + managerToken)
                        .contentType(ContentType.JSON)
                        .when()
                        .get("/api/v1/adoption-request/" + createdRequest.getId())
                        .then()
                        .statusCode(200)
                        .extract().body().as(AdoptionRequestDTO.class);

        assertNotNull(retrievedRequest);
        assertEquals(createdRequest.getId(), retrievedRequest.getId());
    }

    @Test
    void getAllAdoptionRequestsShouldReturn200() {
        AdoptionRequestCreationDTO request1 = new AdoptionRequestCreationDTO(1L, 1L, 1L, State.SENT);
        AdoptionRequestCreationDTO request2 = new AdoptionRequestCreationDTO(1L, 1L, 1L, State.SENT);

        given()
                .header("Authorization", "Bearer " + managerToken)
                .contentType(ContentType.JSON)
                .body(request1)
                .when()
                .post("/api/v1/adoption-request")
                .then()
                .statusCode(201);

        given()
                .header("Authorization", "Bearer " + managerToken)
                .contentType(ContentType.JSON)
                .body(request2)
                .when()
                .post("/api/v1/adoption-request")
                .then()
                .statusCode(201);

        AdoptionRequestDTO[] requests =
                given()
                        .header("Authorization", "Bearer " + managerToken)
                        .when()
                        .get("/api/v1/adoption-request/all")
                        .then()
                        .statusCode(200)
                        .extract().body().as(AdoptionRequestDTO[].class);

        assertTrue(requests.length > 1);
    }

    @Test
    void deleteAdoptionRequestShouldReturn204() {
        AdoptionRequestCreationDTO request = new AdoptionRequestCreationDTO(1L, 1L, 1L, State.SENT);

        AdoptionRequestDTO createdRequest = given()
                .header("Authorization", "Bearer " + managerToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/adoption-request")
                .then()
                .statusCode(201)
                .extract().body().as(AdoptionRequestDTO.class);

        given()
                .header("Authorization", "Bearer " + managerToken)
                .when()
                .delete("/api/v1/adoption-request/delete/" + createdRequest.getId())
                .then()
                .statusCode(204);
    }

    @Test
    void createTwoAdoptionRequests_withSameData_shouldHaveDifferentIds() {
        AdoptionRequestCreationDTO request1 = new AdoptionRequestCreationDTO(1L, 1L, 1L, State.SENT);
        AdoptionRequestCreationDTO request2 = new AdoptionRequestCreationDTO(1L, 1L, 1L, State.SENT);

        Integer id1 = given()
                .header("Authorization", "Bearer " + managerToken)
                .contentType(ContentType.JSON)
                .body(request1)
                .when()
                .post("/api/v1/adoption-request")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        Integer id2 = given()
                .header("Authorization", "Bearer " + managerToken)
                .contentType(ContentType.JSON)
                .body(request2)
                .when()
                .post("/api/v1/adoption-request")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        assertNotEquals(id1, id2);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidAdoptionRequestData")
    void createAdoptionRequest_withInvalidData_shouldReturn400(String adoptionRequestJson, String expectedErrorMessage) {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + managerToken)
                .body(adoptionRequestJson)
                .when()
                .post("/api/v1/adoption-request")
                .then()
                .statusCode(400)
                .extract().body().jsonPath().param("message", expectedErrorMessage);
    }

    private static Stream<Arguments> provideInvalidAdoptionRequestData() {
        return Stream.of(
                // Invalid petId: null
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "personId": 1,
                      "petId": null,
                      "state": "SENT"
                    }
                """, "Pet ID must be provided"),

                // Invalid petId: empty
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "personId": 1,
                      "petId": "",
                      "state": "SENT"
                    }
                """, "Pet ID must be provided"),

                // Invalid petId: negative
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "personId": 1,
                      "petId": -1,
                      "state": "SENT"
                    }
                """, "Pet ID must be greater than 0"),

                // Invalid petId: zero
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "personId": 1,
                      "petId": 0,
                      "state": "SENT"
                    }
                """, "Pet ID must be greater than 0"),

                // Invalid personId: null
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "personId": null,
                      "petId": 1,
                      "state": "SENT"
                    }
                """, "Person ID must be provided"),

                // Invalid personId: negative
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "personId": -1,
                      "petId": 1,
                      "state": "SENT"
                    }
                """, "Person ID must be greater than 0"),

                // Invalid personId: zero
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "personId": 0,
                      "petId": 1,
                      "state": "SENT"
                    }
                """, "Person ID must be greater than 0"),

                // Invalid shelterId: null
                Arguments.of("""
                    {
                      "shelterId": null,
                      "personId": 1,
                      "petId": 1,
                      "state": "SENT"
                    }
                """, "Shelter ID must be provided"),

                // Invalid shelterId: negative
                Arguments.of("""
                    {
                      "shelterId": -1,
                      "personId": 1,
                      "petId": 1,
                      "state": "SENT"
                    }
                """, "Shelter ID must be greater than 0"),

                // Invalid shelterId: zero
                Arguments.of("""
                    {
                      "shelterId": 0,
                      "personId": 1,
                      "petId": 1,
                      "state": "SENT"
                    }
                """, "Shelter ID must be greater than 0"),

                // Invalid state: null (if state is required)
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "personId": 1,
                      "petId": 1,
                      "state": null
                    }
                """, "State must be provided"),

                // Invalid state: empty string
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "personId": 1,
                      "petId": 1,
                      "state": ""
                    }
                """, "Invalid state value"),

                // Invalid state: invalid value
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "personId": 1,
                      "petId": 1,
                      "state": "INVALID_STATE"
                    }
                """, "Invalid state value"),

                // Missing shelterId
                Arguments.of("""
                    {
                      "personId": 1,
                      "petId": 1,
                      "state": "SENT"
                    }
                """, "Shelter ID must be provided"),

                // Missing personId
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "petId": 1,
                      "state": "SENT"
                    }
                """, "Person ID must be provided"),

                // Missing petId
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "personId": 1,
                      "state": "SENT"
                    }
                """, "Pet ID must be provided"),

                // Missing state (if state is required)
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "personId": 1,
                      "petId": 1
                    }
                """, "State must be provided"),

                // All fields null
                Arguments.of("""
                    {
                      "shelterId": null,
                      "personId": null,
                      "petId": null,
                      "state": null
                    }
                """, "Shelter ID must be provided"),

                // All fields missing
                Arguments.of("""
                    {
                    }
                """, "Shelter ID must be provided"),

                // Invalid data types: petId as string
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "personId": 1,
                      "petId": "one",
                      "state": "SENT"
                    }
                """, "Pet ID must be a number"),

                // Invalid data types: personId as string
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "personId": "one",
                      "petId": 1,
                      "state": "SENT"
                    }
                """, "Person ID must be a number"),

                // Invalid data types: shelterId as string
                Arguments.of("""
                    {
                      "shelterId": "one",
                      "personId": 1,
                      "petId": 1,
                      "state": "SENT"
                    }
                """, "Shelter ID must be a number"),

                // Invalid petId: decimal number
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "personId": 1,
                      "petId": 1.5,
                      "state": "SENT"
                    }
                """, "Pet ID must be an integer"),

                // Invalid personId: decimal number
                Arguments.of("""
                    {
                      "shelterId": 1,
                      "personId": 1.5,
                      "petId": 1,
                      "state": "SENT"
                    }
                """, "Person ID must be an integer"),

                // Invalid shelterId: decimal number
                Arguments.of("""
                    {
                      "shelterId": 1.5,
                      "personId": 1,
                      "petId": 1,
                      "state": "SENT"
                    }
                """, "Shelter ID must be an integer")
        );
    }

    @ParameterizedTest
    @MethodSource("provideEndpointsForRolePermissionTests")
    void user_withDifferentRoles_shouldReturnExpectedStatus(String role, String method, String urlTemplate, int expectedStatus) {
        Long adoptionRequestId = createAdoptionRequestAndGetId();

        String token = switch (role) {
            case "ADMIN" -> adminToken;
            case "MANAGER" -> managerToken;
            case "USER" -> userToken;
            default -> throw new IllegalArgumentException("Unsupported role: " + role);
        };

        // Create a request specification with the correct token
        RequestSpecification request = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);

        // Insert the extracted ID into the URL if needed
        String url = urlTemplate.contains("%d") ? String.format(urlTemplate, adoptionRequestId) : urlTemplate;

        // Appropriate body based on the endpoint
        String requestBody = getRequestBody(url);

        // Execute the request based on the HTTP method
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
            case "PATCH":
                request.body(requestBody)
                        .when()
                        .patch(url)
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
        if (url.equals("/api/v1/adoption-request")) {
            return VALID_ADOPTION_REQUEST_JSON; // Valid adoption request creation JSON
        } else if (url.contains("/update")) {
            return """
                {
                  "state": "REFUSED"
                }
            """; // Valid adoption request update JSON
        } else {
            return ""; // For endpoints that don’t need a body, like DELETE
        }
    }

    private static Stream<Arguments> provideEndpointsForRolePermissionTests() {
        return Stream.of(
                // Test POST endpoint
                Arguments.of("MANAGER", "POST", "/api/v1/adoption-request", 201),
                Arguments.of("ADMIN", "POST", "/api/v1/adoption-request", 201),
                Arguments.of("USER", "POST", "/api/v1/adoption-request", 201),

                // Test GET all endpoint
                Arguments.of("MANAGER", "GET", "/api/v1/adoption-request/all", 200),
                Arguments.of("ADMIN", "GET", "/api/v1/adoption-request/all", 200),
                Arguments.of("USER", "GET", "/api/v1/adoption-request/all", 403),

                // Test GET by ID endpoint
                Arguments.of("MANAGER", "GET", "/api/v1/adoption-request/%d", 200),
                Arguments.of("ADMIN", "GET", "/api/v1/adoption-request/%d", 200),
                Arguments.of("USER", "GET", "/api/v1/adoption-request/%d", 403),

                // Test PATCH endpoint
                Arguments.of("MANAGER", "PATCH", "/api/v1/adoption-request/update/%d", 200),
                Arguments.of("ADMIN", "PATCH", "/api/v1/adoption-request/update/%d", 200),
                Arguments.of("USER", "PATCH", "/api/v1/adoption-request/update/%d", 200),

                // Test DELETE endpoint
                Arguments.of("MANAGER", "DELETE", "/api/v1/adoption-request/delete/%d", 204),
                Arguments.of("ADMIN", "DELETE", "/api/v1/adoption-request/delete/%d", 403),
                Arguments.of("USER", "DELETE", "/api/v1/adoption-request/delete/%d", 403)
        );
    }
}
