package org.mindera.fur.code.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.dto.pet.PetDTO;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = RANDOM_PORT)
class PetControllerTest {

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

    private static Stream<Arguments> provideValidPetJson() {
        return Stream.of(
                Arguments.of("""
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
                        """, "Max"),
                Arguments.of("""
                            {
                              "name": "Buddy",
                              "petTypeId": 2,
                              "shelterId": 2,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "MEDIUM",
                              "weight": 18.0,
                              "color": "Black",
                              "age": 4,
                              "observations": "Very playful"
                            }
                        """, "Buddy"),
                Arguments.of("""
                            {
                              "name": "Charlie",
                              "petTypeId": 3,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "SMALL",
                              "weight": 10.0,
                              "color": "White",
                              "age": 2,
                              "observations": "Gentle and calm"
                            }
                        """, "Charlie")
        );
    }

    private static Stream<Arguments> provideEndpointsForRolePermissionTests() {
        return Stream.of(

                // Test POST endpoints
                Arguments.of("ADMIN", "POST", "/api/v1/pet", 201),
                Arguments.of("USER", "POST", "/api/v1/pet", 403),
                Arguments.of("ADMIN", "POST", "/api/v1/pet/%d/create-record", 201),
                Arguments.of("USER", "POST", "/api/v1/pet/%d/create-record", 403),

                // Test GET endpoints
                Arguments.of("ADMIN", "GET", "/api/v1/pet/%d", 200),
                Arguments.of("USER", "GET", "/api/v1/pet/%d", 200), // No security for this endpoint
                Arguments.of("ADMIN", "GET", "/api/v1/pet/%d/record", 200),
                Arguments.of("USER", "GET", "/api/v1/pet/%d/record", 403),

                // Test PUT endpoints
                Arguments.of("ADMIN", "PUT", "/api/v1/pet/update/%d", 200), // Suppose to be 204
                Arguments.of("USER", "PUT", "/api/v1/pet/update/%d", 403),

                // Test DELETE endpoints
                Arguments.of("MANAGER", "DELETE", "/api/v1/pet/delete/%d", 204),
                Arguments.of("ADMIN", "DELETE", "/api/v1/pet/delete/%d", 403),
                Arguments.of("USER", "DELETE", "/api/v1/pet/delete/%d", 403)
        );
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        personRepository.deleteAll();
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
        // Create the pet record using the pet ID and store its ID
        createPetRecordAndGetId(petId);
    }

    @AfterEach
    void tearDown() {
        petRepository.deleteAll();
        personRepository.deleteAll();
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

    // A validPetRecordJson to use globally across multiple tests
    private String createPetRecordJson(Long petId) {
        return """
                    {
                      "petId": %d,
                      "intervention": "Pet was washed and groomed",
                      "createdAt": "2024-08-15T11:08:13.990Z"
                    }
                """.formatted(petId);
    }

    private Long createPetAndGetId() {
        Integer idAsInteger = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken) // adminToken is used
                .body(VALID_PET_JSON)
                .when()
                .post("/api/v1/pet")
                .then()
                .statusCode(201)
                .extract()
                .path("id"); // Extract and return the pet's ID

        return idAsInteger.longValue();
    }

    private void createPetRecordAndGetId(Long petId) {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(createPetRecordJson(petId))
                .when()
                .post(String.format("/api/v1/pet/%d/create-record", petId))
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    @Test
    void createThreePets_withSameData_shouldHaveDifferentIds() {

        PetDTO dto1 = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken) // Use the admin token
                .body(VALID_PET_JSON)
                .when()
                .post("/api/v1/pet")
                .then()
                .statusCode(201)
                .extract().body().as(PetDTO.class);

        PetDTO dto2 = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(VALID_PET_JSON)
                .when()
                .post("/api/v1/pet")
                .then()
                .statusCode(201)
                .extract().body().as(PetDTO.class);

        // IDs are 2 and 3 because the first pet is created in beforeEach
        assertEquals(2, dto1.getId());
        assertEquals(3, dto2.getId());

        assertEquals("Max", dto1.getName());
        assertEquals(dto1.getName(), dto2.getName());
        assertEquals(dto1.getPetTypeId(), dto2.getPetTypeId());
        assertEquals(dto1.getShelterId(), dto2.getShelterId());
        assertEquals(dto1.getIsAdopted(), dto2.getIsAdopted());
        assertEquals(dto1.getIsVaccinated(), dto2.getIsVaccinated());
        assertEquals(dto1.getWeight(), dto2.getWeight(), 0.01);
        assertEquals(dto1.getColor(), dto2.getColor());
        assertEquals(dto1.getAge(), dto2.getAge());
        assertEquals(dto1.getObservations(), dto2.getObservations());
    }

    @Test
    void createAndRetrievePetRecord() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/api/v1/pet/" + petId + "/record")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0)); // Assert that at least one record is returned
    }

    @ParameterizedTest
    @MethodSource("provideValidPetJson")
    void createMultiplePets_withValidData_shouldReturn201(String petJson, String expectedName) {
        // Perform the POST request to create the pet
        PetDTO dto = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(petJson)
                .when()
                .post("/api/v1/pet")
                .then()
                .statusCode(201)
                .extract().body().as(PetDTO.class);

        // Perform assertions based on the extracted PetDTO
        assertEquals(expectedName, dto.getName());

        switch (expectedName) {
            case "Max":
                assertEquals(1, dto.getPetTypeId());
                assertEquals(1, dto.getShelterId());
                assertFalse(dto.getIsAdopted());
                assertTrue(dto.getIsVaccinated());
                assertEquals(25.5, dto.getWeight(), 0.01);
                assertEquals("Brown", dto.getColor());
                assertEquals(3, dto.getAge());
                assertEquals("Healthy and active", dto.getObservations());
                break;
            case "Buddy":
                assertEquals(2, dto.getPetTypeId());
                assertEquals(2, dto.getShelterId());
                assertFalse(dto.getIsAdopted());
                assertTrue(dto.getIsVaccinated());
                assertEquals(18.0, dto.getWeight(), 0.01);
                assertEquals("Black", dto.getColor());
                assertEquals(4, dto.getAge());
                assertEquals("Very playful", dto.getObservations());
                break;
            case "Charlie":
                assertEquals(3, dto.getPetTypeId());
                assertEquals(1, dto.getShelterId());
                assertFalse(dto.getIsAdopted());
                assertTrue(dto.getIsVaccinated());
                assertEquals(10.0, dto.getWeight(), 0.01);
                assertEquals("White", dto.getColor());
                assertEquals(2, dto.getAge());
                assertEquals("Gentle and calm", dto.getObservations());
                break;
            default:
                throw new IllegalArgumentException("Unexpected pet name: " + expectedName);
        }
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPetData")
    void createPet_withInvalidData_shouldReturn400(String petJson, String expectedErrorMessage) {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken) // Admin token
                .body(petJson)
                .when()
                .post("/api/v1/pet")
                .then()
                .statusCode(400)
                .extract().body().jsonPath().param("message", expectedErrorMessage);
    }

    private Stream<Arguments> provideInvalidPetData() {
        return Stream.of(

                // Invalid name: integer
                Arguments.of("""
                            {
                              "name": 33,.repeat(300),
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
                        """, "Pet name must be provided"),

                // Invalid name: null
                Arguments.of("""
                            {
                              "name": null,
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
                        """, "Pet name must be provided"),

                // Invalid name: too short
                Arguments.of("""
                            {
                              "name": "",
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
                        """, "Pet name must be between 1 and 30 characters"),

                // Invalid name: too high
                Arguments.of("""
                            {
                              "name": "A".repeat(31),
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
                        """, "Pet name must be between 1 and 30 characters"),

                // Invalid petTypeId: integer zero
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": 0,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "LARGE",
                              "weight": 25.5,
                              "color": "Brown",
                              "age": 3,
                              "observations": "Healthy and active"
                            }
                        """, "Pet type ID must be provided"),

                // Invalid petTypeId: negative integer
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": -1,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "LARGE",
                              "weight": 25.5,
                              "color": "Brown",
                              "age": 3,
                              "observations": "Healthy and active"
                            }
                        """, "Pet type ID must be provided"),

                // Invalid petTypeId: null
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": null,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "LARGE",
                              "weight": 25.5,
                              "color": "Brown",
                              "age": 3,
                              "observations": "Healthy and active"
                            }
                        """, "Pet type ID must be provided"),

                // Invalid shelterId: null
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": 1,
                              "shelterId": null,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "LARGE",
                              "weight": 25.5,
                              "color": "Brown",
                              "age": 3,
                              "observations": "Healthy and active"
                            }
                        """, "Shelter ID must be provided"),

                // Invalid isAdopted: null
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": 1,
                              "shelterId": 1,
                              "isAdopted": null,
                              "isVaccinated": true,
                              "size": "LARGE",
                              "weight": 25.5,
                              "color": "Brown",
                              "age": 3,
                              "observations": "Healthy and active"
                            }
                        """, "Adopted status must be provided"),

                // Invalid isVaccinated: null
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": 1,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": null,
                              "size": "LARGE",
                              "weight": 25.5,
                              "color": "Brown",
                              "age": 3,
                              "observations": "Healthy and active"
                            }
                        """, "Vaccination status is required"),

                // Invalid size enum: null
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": 1,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": null,
                              "weight": 25.5,
                              "color": "Brown",
                              "age": 3,
                              "observations": "Healthy and active"
                            }
                        """, "Size must be provided"),

                // Invalid weight: null
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": 1,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "LARGE",
                              "weight": null,
                              "color": "Brown",
                              "age": 3,
                              "observations": "Healthy and active"
                            }
                        """, "Pet weight must be provided"),

                // Invalid weight: too low
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": 1,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "LARGE",
                              "weight": 0.0,
                              "color": "Brown",
                              "age": 3,
                              "observations": "Healthy and active"
                            }
                        """, "Pet weight must be greater than 0.01 kilos"),

                // Invalid weight: too high
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": 1,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "LARGE",
                              "weight": 1000.0,
                              "color": "Brown",
                              "age": 3,
                              "observations": "Healthy and active"
                            }
                        """, "Pet weight must be less than 999.99 kilos"),

                // Invalid color: null
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": 1,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "LARGE",
                              "weight": 25.5,
                              "color": null,
                              "age": 3,
                              "observations": "Healthy and active"
                            }
                        """, "Pet color must be provided"),

                // Invalid color: empty
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": 1,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "LARGE",
                              "weight": 25.5,
                              "color": "",
                              "age": 3,
                              "observations": "Healthy and active"
                            }
                        """, "Pet color must be provided"),

                // Invalid color: too short
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": 1,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "LARGE",
                              "weight": 25.5,
                              "color": "Br",
                              "age": 3,
                              "observations": "Healthy and active"
                            }
                        """, "Pet color must be between 3 and 99 characters"),

                // Invalid color: too high
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": 1,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "LARGE",
                              "weight": 25.5,
                              "color": "B",.repeat(100),
                              "age": 3,
                              "observations": "Healthy and active"
                            }
                        """, "Pet color must be between 3 and 99 characters"),

                // Invalid age: null
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": 1,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "LARGE",
                              "weight": 25.5,
                              "color": "Brown",
                              "age": null,
                              "observations": "Healthy and active"
                            }
                        """, "Pet age must be provided"),

                // Invalid age: too low
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": 1,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "LARGE",
                              "weight": 25.5,
                              "color": "Brown",
                              "age": 0,
                              "observations": "Healthy and active"
                            }
                        """, "Pet age must be greater than 1"),

                // Invalid age: too high
                Arguments.of("""
                            {
                              "name": "Max",
                              "petTypeId": 1,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "LARGE",
                              "weight": 25.5,
                              "color": "Brown",
                              "age": 100,
                              "observations": "Healthy and active"
                            }
                        """, "Pet age must be less than 99"),

                // Invalid observations: null
                Arguments.of("""
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
                              "observations": null
                            }
                        """, "Pet observation must be provided"),

                // Invalid observations: too low
                Arguments.of("""
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
                              "observations": ""
                            }
                        """, "Pet observation must be provided"),

                // Invalid observations: too high
                Arguments.of("""
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
                              "observations": "C".repeat(1000)
                            }
                        """, "Pet observation must be between 1 and 999 characters")
        );
    }

    @ParameterizedTest
    @MethodSource("provideEndpointsForRolePermissionTests")
    void user_withDifferentRoles_shouldReturnExpectedStatus(String role, String method, String urlTemplate, int expectedStatus) {
        // Determine the token based on the role
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

        // Insert the extracted ID into the URL
        String url = String.format(urlTemplate, petId);

        // Appropriated body based on the endpoint
        String requestBody = getString(url);

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
    private String getString(String url) {
        String requestBody;
        if (url.contains("/create-record")) {
            requestBody = """
                        {
                          "petId": %d,
                          "intervention": "Pet was vaccinated",
                          "createdAt": "2024-08-15T11:08:13.990Z"
                        }
                    """.formatted(petId);
        } else if (url.equals("/api/v1/pet")) {
            requestBody = VALID_PET_JSON; // Valid pet creation JSON
        } else if (url.contains("/update")) {
            requestBody = """
                        {
                        "_comment": "missing fields are intentionally left out: color, isVaccinated, size.",
                          "isAdopted": false,
                          "weight": 9.9,
                          "age": 3,
                          "observations": "Healthy"
                        }
                    """; // Valid pet update JSON
        } else {
            requestBody = ""; // For endpoints that don’t need a body, like DELETE
        }
        return requestBody;
    }
}
