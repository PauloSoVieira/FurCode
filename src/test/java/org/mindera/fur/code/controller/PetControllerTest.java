package org.mindera.fur.code.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.mindera.fur.code.dto.pet.PetRecordDTO;
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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class PetControllerTest {

    private String managerToken;
    private String adminToken;
    private String userToken;

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

    @Disabled
    @ParameterizedTest
    @MethodSource("provideValidPetJson")
    void createMultiplePets_withValidData_shouldReturn201(String petJson, String expectedName) {

        PetDTO dto = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken) // Use the admin token
                .body(petJson)
                .when()
                .post("/api/v1/pet")
                .then()
                .statusCode(201)
                .extract().body().as(PetDTO.class);

        assertEquals(1, dto.getId());
        assertEquals(expectedName, dto.getName());
        assertEquals(1, dto.getPetTypeId());
        assertEquals(1, dto.getShelterId());
        assertFalse(dto.getIsAdopted());
        assertTrue(dto.getIsVaccinated());
        assertEquals(25.5, dto.getWeight(), 0.01);
        assertEquals("Brown", dto.getColor());
        assertEquals(3, dto.getAge());
        assertEquals("Healthy and active", dto.getObservations());
    }

    private Stream<Arguments> provideValidPetJson() {
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
                              "petTypeId": 1,
                              "shelterId": 1,
                              "isAdopted": false,
                              "isVaccinated": true,
                              "size": "MEDIUM",
                              "weight": 18.0,
                              "color": "Black",
                              "age": 4,
                              "observations": "Very playful"
                            }
                        """, "Buddy")
        );
    }

    @Disabled
    @ParameterizedTest
    @MethodSource("provideInvalidPetData")
    void createPet_withInvalidData_shouldReturn400(String petJson, String expectedErrorMessage) {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken) // Use the admin token
                .body(petJson)
                .when()
                .post("/api/v1/pet")
                .then()
                .statusCode(400)
                .extract().body().jsonPath().param("message", expectedErrorMessage);
    }

    private Stream<Arguments> provideInvalidPetData() {
        return Stream.of(

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








    @Disabled
    @ParameterizedTest
    @MethodSource("provideEndpointsForUserPermissionTests")
    void user_withNoPermissions_shouldReceive403(String method, String url) {
        RequestSpecification request = given()
                .header("Authorization", "Bearer " + userToken)
                .contentType(ContentType.JSON);

        switch (method) {
            case "POST":
                request.body("{}")
                        .when()
                        .post(url)
                        .then()
                        .statusCode(403);
                break;
            case "PUT":
                request.body("{}")
                        .when()
                        .put(url)
                        .then()
                        .statusCode(403);
                break;
            case "DELETE":
                request.when()
                        .delete(url)
                        .then()
                        .statusCode(403);
                break;
            case "GET":
                request.when()
                        .get(url)
                        .then()
                        .statusCode(403);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }

    // Endpoints and to be tested
    private static Stream<Arguments> provideEndpointsForUserPermissionTests() {
        return Stream.of(
                // Test POST endpoints
                Arguments.of("POST", "/api/v1/pet"),
                Arguments.of("POST", "/api/v1/pet/1/create-record"),

                // Test PUT endpoint
                Arguments.of("PUT", "/api/v1/pet/update/1"),

                // Test DELETE endpoint
                Arguments.of("DELETE", "/api/v1/pet/delete/1"),

                // Test GET endpoint
                Arguments.of("GET", "/api/v1/pet/1/record")
        );
    }

    @ParameterizedTest
    @MethodSource("provideEndpointsForAdminPermissionTests")
    void admin_withProperPermissions_shouldAccessEndpointsSuccessfully(String method, String url, int expectedStatus) {
        String validPetJson = """
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

        RequestSpecification request = given()
                .header("Authorization", "Bearer " + adminToken) // Use the admin token
                .contentType(ContentType.JSON);

        // Execute the request based on the HTTP method
        switch (method) {
            case "POST":
                request.body(validPetJson)
                        .when()
                        .post(url)
                        .then()
                        .statusCode(expectedStatus); // Expecting a successful status
                break;
            case "PUT":
                request.body(validPetJson)
                        .when()
                        .put(validPetJson)
                        .then()
                        .statusCode(expectedStatus);
                break;
            case "DELETE":
                request.when()
                        .delete(url)
                        .then()
                        .statusCode(expectedStatus);
                break;
            case "GET":
                request.when()
                        .get(url)
                        .then()
                        .statusCode(expectedStatus);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }

    private static Stream<Arguments> provideEndpointsForAdminPermissionTests() {
        return Stream.of(
                // Test POST endpoints
                Arguments.of("POST", "/api/v1/pet", 201),
                Arguments.of("POST", "/api/v1/pet/1/create-record", 201),

                // Test PUT endpoint
                Arguments.of("PUT", "/api/v1/pet/update/1", 200),

                // Test DELETE endpoint
                Arguments.of("DELETE", "/api/v1/pet/delete/1", 204),

                // Test GET endpoint
                Arguments.of("GET", "/api/v1/pet/1/record", 200)
        );
    }







    @Disabled
    @Test
    void createMultiplePets_withValidData_shouldReturn201() {
        String petJson1 = """
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

        String petJson2 = """
        {
          "name": "Buddy",
          "petTypeId": 1,
          "shelterId": 1,
          "isAdopted": true,
          "isVaccinated": false,
          "size": "MEDIUM",
          "weight": 18.0,
          "color": "Black",
          "age": 4,
          "observations": "Very playful"
        }
    """;

        PetDTO dto1 = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(petJson1)
                .when()
                .post("/api/v1/pet")
                .then()
                .statusCode(201)
                .extract().body().as(PetDTO.class);

        PetDTO dto2 = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(petJson2)
                .when()
                .post("/api/v1/pet")
                .then()
                .statusCode(201)
                .extract().body().as(PetDTO.class);

        assertEquals(1, dto1.getId());
        assertEquals("Max", dto1.getName());
        assertEquals(1, dto1.getPetTypeId());
        assertEquals(1, dto1.getShelterId());
        assertFalse(dto1.getIsAdopted());
        assertTrue(dto1.getIsVaccinated());
        assertEquals(25.5, dto1.getWeight(), 0.01);
        assertEquals("Brown", dto1.getColor());
        assertEquals(3, dto1.getAge());
        assertEquals("Healthy and active", dto1.getObservations());

        assertEquals(2, dto2.getId());
        assertNotEquals(dto1.getId(), dto2.getId());
        assertEquals("Buddy", dto2.getName());
        assertEquals(1, dto2.getPetTypeId());
        assertEquals(1, dto2.getShelterId());
        assertTrue(dto2.getIsAdopted());
        assertFalse(dto2.getIsVaccinated());
        assertEquals(18.0, dto2.getWeight(), 0.01);
        assertEquals("Black", dto2.getColor());
        assertEquals(4, dto2.getAge());
        assertEquals("Very playful", dto2.getObservations());
    }

    @Disabled
    @Test
    void createTwoPets_withSameData_shouldHaveDifferentIds() {
        String petJson = """
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

        PetDTO dto1 = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken) // Use the admin token
                .body(petJson)
                .when()
                .post("/api/v1/pet")
                .then()
                .statusCode(201)
                .extract().body().as(PetDTO.class);

        PetDTO dto2 = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken) // Use the admin token
                .body(petJson)
                .when()
                .post("/api/v1/pet")
                .then()
                .statusCode(201)
                .extract().body().as(PetDTO.class);

        assertEquals(1, dto1.getId());
        assertEquals(2, dto2.getId());

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

    @Disabled
    @Test
    void deletePet_withManagerRole_shouldReturn204() {
        given()
                .header("Authorization", "Bearer " + managerToken) // Manager role token
                .when()
                .delete("/api/v1/pet/delete/1") // Assuming pet ID 1 exists
                .then()
                .statusCode(204); // Expecting 204 No Content for MANAGER role
    }

    @Disabled
    @Test
    void deletePet_withNonManagerRole_shouldReturn403() {
        given()
                .header("Authorization", "Bearer " + userToken) // Regular user token
                .when()
                .delete("/api/v1/pet/delete/1") // Assuming pet ID 1 exists
                .then()
                .statusCode(403); // Expecting 403 Forbidden for non-MANAGER role
    }













    private void createTestPets() {
        if (!petTypeRepository.existsById(2L)) {
            PetType petType = new PetType();
            petType.setId(2L);
            petType.setSpecies(PetSpeciesEnum.DOG);
            petType.setBreed(petBreedRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("Breed not found")));
            petTypeRepository.save(petType);
        }

        given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "Buddy",
                    "petTypeId": 1,
                    "shelterId": 1,
                    "isAdopted": false,
                    "size": "MEDIUM",
                    "weight": 20.0,
                    "color": "Brown",
                    "age": 3,
                    "observations": "Healthy and active"
                  }
                  """
                )
                .when()
                .post()
                .then()
                .statusCode(201);

        given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "Bobby",
                    "petTypeId": 2,
                    "shelterId": 1,
                    "isAdopted": false,
                    "size": "MEDIUM",
                    "weight": 20.0,
                    "color": "Black",
                    "age": 3,
                    "observations": "Healthy and active"
                  }
                  """
                )
                .when()
                .post()
                .then()
                .statusCode(201);
    }

    @Disabled
    @Test
    void createPet_givenValidInput_shouldReturn201() {
        given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "Buddy",
                    "petTypeId": 1,
                    "shelterId": 1,
                    "isAdopted": false,
                    "size": "MEDIUM",
                    "weight": 20.0,
                    "color": "Brown",
                    "age": 3,
                    "observations": "Healthy and active"
                  }
                  """
                )
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Buddy"))
                .body("petTypeId", equalTo(1))
                .body("shelterId", equalTo(1))
                .body("isAdopted", equalTo(false))
                .body("size", equalTo("MEDIUM"))
                .body("weight", equalTo(20.0f))
                .body("color", equalTo("Brown"))
                .body("age", equalTo(3))
                .body("observations", equalTo("Healthy and active"));
    }

    @Disabled
    @Test
    void createPet_givenEmptyName_shouldReturn400() {
        given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "",
                    "petTypeId": 1,
                    "shelterId": 1,
                    "isAdopted": false,
                    "size": "MEDIUM",
                    "weight": 20.0,
                    "color": "Brown",
                    "age": 3,
                    "observations": "Healthy and active"
                  }
                  """
                )
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Disabled
    @Test
    void createPet_givenInvalidPetTypeId_shouldReturn400() {
        given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "Buddy",
                    "petTypeId": null,
                    "shelterId": 1,
                    "isAdopted": false,
                    "size": "MEDIUM",
                    "weight": 20.0,
                    "color": "Brown",
                    "age": 3,
                    "observations": "Healthy and active"
                  }
                  """
                )
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Disabled
    @Test
    void createPet_givenInvalidShelterId_shouldReturn400() {
        given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "Buddy",
                    "petTypeId": 1,
                    "shelterId": null,
                    "isAdopted": false,
                    "size": "MEDIUM",
                    "weight": 20.0,
                    "color": "Brown",
                    "age": 3,
                    "observations": "Healthy and active"
                  }
                  """
                )
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Disabled
    @Test
    void createPet_givenInvalidAdoptedStatus_shouldReturn400() {
        given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "Buddy",
                    "petTypeId": 1,
                    "shelterId": 1,
                    "isAdopted": null,
                    "size": "MEDIUM",
                    "weight": 20.0,
                    "color": "Brown",
                    "age": 3,
                    "observations": "Healthy and active"
                  }
                  """
                )
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Disabled
    @Test
    void createPet_givenEmptySize_shouldReturn400() {
        given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "Buddy",
                    "petTypeId": 1,
                    "shelterId": 1,
                    "isAdopted": false,
                    "size": "",
                    "weight": 20.0,
                    "color": "Brown",
                    "age": 3,
                    "observations": "Healthy and active"
                  }
                  """
                )
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Disabled
    @Test
    void createPet_givenInvalidWeight_shouldReturn400() {
        given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "Buddy",
                    "petTypeId": 1,
                    "shelterId": 1,
                    "isAdopted": false,
                    "size": "MEDIUM",
                    "weight": -1.0,
                    "color": "Brown",
                    "age": 3,
                    "observations": "Healthy and active"
                  }
                  """
                )
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Disabled
    @Test
    void createPet_givenEmptyColor_shouldReturn400() {
        given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "Buddy",
                    "petTypeId": 1,
                    "shelterId": 1,
                    "isAdopted": false,
                    "size": "MEDIUM",
                    "weight": 20.0,
                    "color": "",
                    "age": 3,
                    "observations": "Healthy and active"
                  }
                  """
                )
                .when()
                .post()
                .then()
                .statusCode(400);
    }
    @Disabled
    @Test
    void createPet_givenInvalidAge_shouldReturn400() {
        given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "Buddy",
                    "petTypeId": 1,
                    "shelterId": 1,
                    "isAdopted": false,
                    "size": "MEDIUM",
                    "weight": 20.0,
                    "color": "Brown",
                    "age": 0,
                    "observations": "Healthy and active"
                  }
                  """
                )
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Disabled
    @Test
    void createPet_givenEmptyObservations_shouldReturn400() {
        given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "Buddy",
                    "petTypeId": 1,
                    "shelterId": 1,
                    "isAdopted": false,
                    "size": "MEDIUM",
                    "weight": 20.0,
                    "color": "Brown",
                    "age": 3,
                    "observations": ""
                  }
                  """
                )
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Disabled
    @Test
    void createPet_givenObservationsExceedingMaxLength_shouldReturn400() {
        given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "Buddy",
                    "petTypeId": 1,
                    "shelterId": 1,
                    "isAdopted": false,
                    "size": "MEDIUM",
                    "weight": 20.0,
                    "color": "Brown",
                    "age": 3,
                    "observations": "A".repeat(1000)
                  }
                  """
                )
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Disabled
    @Test
    void getAllPets_givenValidRequest_shouldReturnAllPets() {
        createTestPets();

        List<PetDTO> petDTOs = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/all")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath().getList(".", PetDTO.class);

        assertEquals(2, petDTOs.size());

        assertEquals("Buddy", petDTOs.get(0).getName());
        assertEquals(1, petDTOs.get(0).getPetTypeId());
        assertEquals(1, petDTOs.get(0).getShelterId());
        assertEquals(false, petDTOs.get(0).getIsAdopted());
        assertEquals("MEDIUM", petDTOs.get(0).getSize());
        assertEquals(20.0, petDTOs.get(0).getWeight());
        assertEquals("Brown", petDTOs.get(0).getColor());
        assertEquals(3, petDTOs.get(0).getAge());
        assertEquals("Healthy and active", petDTOs.get(0).getObservations());

        assertEquals("Bobby", petDTOs.get(1).getName());
        assertEquals(2, petDTOs.get(1).getPetTypeId());
        assertEquals(1, petDTOs.get(1).getShelterId());
        assertEquals(false, petDTOs.get(1).getIsAdopted());
        assertEquals("MEDIUM", petDTOs.get(1).getSize());
        assertEquals(20.0, petDTOs.get(1).getWeight());
        assertEquals("Black", petDTOs.get(1).getColor());
        assertEquals(3, petDTOs.get(1).getAge());
        assertEquals("Healthy and active", petDTOs.get(1).getObservations());
    }

    @Disabled
    @Test
    void getPetById_givenExistingPet_shouldReturnPetDetails() {
        given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "Buddy",
                    "petTypeId": 1,
                    "shelterId": 1,
                    "isAdopted": false,
                    "size": "MEDIUM",
                    "weight": 20.0,
                    "color": "Brown",
                    "age": 3,
                    "observations": "Healthy and active"
                  }
                  """
                )
                .when()
                .post()
                .then()
                .statusCode(201);

        when()
                .get("/{id}", 1)
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Buddy"))
                .body("petTypeId", equalTo(1))
                .body("shelterId", equalTo(1))
                .body("isAdopted", equalTo(false))
                .body("size", equalTo("MEDIUM"))
                .body("weight", equalTo(20.0f))
                .body("color", equalTo("Brown"))
                .body("age", equalTo(3))
                .body("observations", equalTo("Healthy and active"));
    }

    @Disabled
    @Test
    void updatePet_givenValidInput_shouldReturn204() {
        PetDTO pet = given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "Bobby",
                    "petTypeId": 1,
                    "shelterId": 1,
                    "isAdopted": false,
                    "size": "MEDIUM",
                    "weight": 20.0,
                    "color": "Brown",
                    "age": 3,
                    "observations": "Healthy and active"
                  }
                  """
                )
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract().as(PetDTO.class);

        PetDTO petUpdated = given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "Bobby Updated",
                    "petTypeId": 1,
                    "shelterId": 1,
                    "isAdopted": true,
                    "size": "LARGE",
                    "weight": 25.0,
                    "color": "Brown",
                    "age": 4,
                    "observations": "Very healthy and active"
                  }
                  """
                )
                .when()
                .put("/update/{id}", pet.getId())
                .then()
                .statusCode(204)
                .extract().as(PetDTO.class);

        when()
                .get("/{id}", petUpdated.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo("Bobby Updated"))
                .body("isAdopted", equalTo(true))
                .body("size", equalTo("LARGE"))
                .body("weight", equalTo(25.0f))
                .body("color", equalTo("Brown"))
                .body("age", equalTo(4))
                .body("observations", equalTo("Very healthy and active"));
    }

    @Disabled
    @Test
    void deletePet_givenExistingPet_shouldReturn404() {
        PetDTO pet = given().contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "Buddy",
                    "petTypeId": 1,
                    "shelterId": 1,
                    "isAdopted": false,
                    "size": "MEDIUM",
                    "weight": 20.0,
                    "color": "Brown",
                    "age": 3,
                    "observations": "Healthy and active"
                  }
                  """
                )
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract().as(PetDTO.class);

        when()
                .delete("/delete/{id}", pet.getId())
                .then()
                .statusCode(204);

        when()
                .get("/{id}", pet.getId())
                .then()
                .statusCode(404);
    }

    @Disabled
    @Test
    void createAndRetrievePetRecord() {
        // First, create a pet
        PetDTO pet = given().contentType(ContentType.JSON)
                .body("""
              {
                "name": "Buddy",
                "petTypeId": 1,
                "shelterId": 1,
                "isAdopted": false,
                "size": "MEDIUM",
                "weight": 20.0,
                "color": "Brown",
                "age": 3,
                "observations": "Healthy and active"
              }
              """)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract().as(PetDTO.class);

        // Then, create a pet record for the pet
        PetRecordDTO petRecord = given().contentType(ContentType.JSON)
                .body("""
              {
                "isVaccinated": true,
                "petRecordsStatus": "Initial Checkup",
                "date": "2024-01-01",
                "observation": "Healthy and active"
              }
              """)
                .when()
                .post("/{id}/create-record", pet.getId())
                .then()
                .statusCode(201)
                .extract().as(PetRecordDTO.class);

        // Finally, retrieve the pet records by pet ID
        List<PetRecordDTO> records = given().contentType(ContentType.JSON)
                .when()
                .get("/{id}/record", pet.getId())
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath().getList(".", PetRecordDTO.class);

        assertFalse(records.isEmpty());
        assertEquals(petRecord.getId(), records.get(0).getId());
    }
}
