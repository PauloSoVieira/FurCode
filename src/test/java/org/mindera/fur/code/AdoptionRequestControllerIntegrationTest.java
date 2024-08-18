package org.mindera.fur.code;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailCreationDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailDTO;
import org.mindera.fur.code.model.Role;
import org.mindera.fur.code.model.State;
import org.mindera.fur.code.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class AdoptionRequestControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    private String userToken;

    @Autowired
    private PersonService personService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
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
    class CrudAdoptionRequest {
        @Test
        void createAdoptionRequestShouldReturn201() {
            Set<RequestDetailDTO> requestDetails = new HashSet<>();
            requestDetails.add(new RequestDetailDTO(1L, 1L, State.SENT, LocalDate.now(), "Test observation"));

            AdoptionRequestCreationDTO request = new AdoptionRequestCreationDTO(
                    1L, // shelterId
                    1L, // personId
                    1L, // petId
                    requestDetails
            );

            AdoptionRequestDTO createdRequest =
                    given()
                            .header("Authorization", "Bearer " + userToken)
                            .contentType(ContentType.JSON)
                            .body(request)
                            .when()
                            .post("/api/v1/adoption-request")
                            .then()
                            .statusCode(201)
                            .extract().body().as(AdoptionRequestDTO.class);

            assertNotNull(createdRequest);
            assertNotNull(createdRequest.getId());
            assertEquals(request.getShelterId(), createdRequest.getShelterId());
            assertEquals(request.getPersonId(), createdRequest.getPersonId());
            assertEquals(request.getPetId(), createdRequest.getPetId());
            assertFalse(createdRequest.getRequestDetails().isEmpty());
        }

        @Test
        void getAdoptionRequestByIdShouldReturn200() {
            Set<RequestDetailDTO> requestDetails = new HashSet<>();
            requestDetails.add(new RequestDetailDTO(1L, 1L, State.SENT, LocalDate.now(), "Test observation"));
            AdoptionRequestCreationDTO request = new AdoptionRequestCreationDTO(1L, 1L, 1L, requestDetails);

            AdoptionRequestDTO createdRequest = given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/api/v1/adoption-request")
                    .then()
                    .statusCode(201)
                    .extract().body().as(AdoptionRequestDTO.class);

            AdoptionRequestDTO retrievedRequest =
                    given()
                            .header("Authorization", "Bearer " + userToken)
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
            // Create an adoption request first
            Set<RequestDetailDTO> requestDetails = new HashSet<>();
            requestDetails.add(new RequestDetailDTO(1L, 1L, State.SENT, LocalDate.now(), "Test observation"));
            AdoptionRequestCreationDTO request = new AdoptionRequestCreationDTO(1L, 1L, 1L, requestDetails);

            given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/api/v1/adoption-request")
                    .then()
                    .statusCode(201);

            AdoptionRequestDTO[] requests =
                    given()
                            .header("Authorization", "Bearer " + userToken)
                            .when()
                            .get("/api/v1/adoption-request/all")
                            .then()
                            .statusCode(200)
                            .extract().body().as(AdoptionRequestDTO[].class);

            assertTrue(requests.length > 0);
        }

        @Test
        void deleteAdoptionRequestShouldReturn204() {
            Set<RequestDetailDTO> requestDetails = new HashSet<>();
            requestDetails.add(new RequestDetailDTO(1L, 1L, State.SENT, LocalDate.now(), "Test observation"));
            AdoptionRequestCreationDTO request = new AdoptionRequestCreationDTO(1L, 1L, 1L, requestDetails);

            AdoptionRequestDTO createdRequest = given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/api/v1/adoption-request")
                    .then()
                    .statusCode(201)
                    .extract().body().as(AdoptionRequestDTO.class);

            given()
                    .header("Authorization", "Bearer " + userToken)
                    .when()
                    .delete("/api/v1/adoption-request/delete/" + createdRequest.getId())
                    .then()
                    .statusCode(204);
        }
    }

    @Nested
    class RequestDetails {
        @Test
        void createRequestDetailShouldReturn201() {
            Set<RequestDetailDTO> requestDetails = new HashSet<>();
            requestDetails.add(new RequestDetailDTO(1L, 1L, State.SENT, LocalDate.now(), "Test observation"));
            AdoptionRequestCreationDTO request = new AdoptionRequestCreationDTO(1L, 1L, 1L, requestDetails);

            AdoptionRequestDTO createdRequest = given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/api/v1/adoption-request")
                    .then()
                    .statusCode(201)
                    .extract().body().as(AdoptionRequestDTO.class);

            RequestDetailCreationDTO detailRequest = new RequestDetailCreationDTO(
                    1L,
                    State.SENT,
                    LocalDate.now(),
                    "Test observation"
            );

            RequestDetailDTO createdDetail =
                    given()
                            .header("Authorization", "Bearer " + userToken)
                            .contentType(ContentType.JSON)
                            .body(detailRequest)
                            .when()
                            .post("/api/v1/adoption-request/" + createdRequest.getId() + "/detail-request")
                            .then()
                            .statusCode(201)
                            .extract().body().as(RequestDetailDTO.class);

            assertNotNull(createdDetail);
            assertEquals(detailRequest.getState(), createdDetail.getState());
        }

        @Test
        void getRequestDetailByIdShouldReturn200() {
            Set<RequestDetailDTO> requestDetails = new HashSet<>();
            requestDetails.add(new RequestDetailDTO(1L, 1L, State.SENT, LocalDate.now(), "Test observation"));
            AdoptionRequestCreationDTO request = new AdoptionRequestCreationDTO(1L, 1L, 1L, requestDetails);

            AdoptionRequestDTO createdRequest = given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/api/v1/adoption-request")
                    .then()
                    .statusCode(201)
                    .extract().body().as(AdoptionRequestDTO.class);

            RequestDetailCreationDTO detailRequest = new RequestDetailCreationDTO(1L, State.SENT, LocalDate.now(), "Test observation");
            RequestDetailDTO createdDetail = given()
                    .header("Authorization", "Bearer " + userToken)
                    .contentType(ContentType.JSON)
                    .body(detailRequest)
                    .when()
                    .post("/api/v1/adoption-request/" + createdRequest.getId() + "/detail-request")
                    .then()
                    .statusCode(201)
                    .extract().body().as(RequestDetailDTO.class);

            RequestDetailDTO retrievedDetail =
                    given()
                            .header("Authorization", "Bearer " + userToken)
                            .when()
                            .get("/api/v1/adoption-request/" + createdRequest.getId() + "/detail-request/" + createdDetail.getId())
                            .then()
                            .statusCode(200)
                            .extract().body().as(RequestDetailDTO.class);

            assertNotNull(retrievedDetail);
            assertEquals(createdDetail.getId(), retrievedDetail.getId());
        }
    }
}