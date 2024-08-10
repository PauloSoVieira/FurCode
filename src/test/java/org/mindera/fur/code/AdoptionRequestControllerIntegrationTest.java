package org.mindera.fur.code;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestDTO;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.dto.pet.PetCreateDTO;
import org.mindera.fur.code.dto.pet.PetRecordCreateDTO;
import org.mindera.fur.code.dto.pet.PetTypeDTO;
import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.RequestDetailRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.mindera.fur.code.service.AdoptionRequestService;
import org.mindera.fur.code.service.RequestDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AdoptionRequestControllerIntegrationTest {


    @LocalServerPort
    private Integer port;

    @Autowired
    private AdoptionRequestService adoptionRequestService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RequestDetailRepository requestDetailRepository;

    @Autowired
    private RequestDetailService requestDetailService;

    @Autowired
    private PetRepository petRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        adoptionRequestService.deleteAllAdoptionRequests();
    }

    @Nested
    class crudAdoptionRequest {
        @Test
        public void createAdoptionRequestShouldReturn201() {

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

            Date createdAt = new Date();
            ShelterCreationDTO shelterCreationDTO = new ShelterCreationDTO(
                    "Shelter",
                    123456789L,
                    "shelter@shelter.com",
                    "Shelter Street",
                    "number",
                    "4400",
                    987654321L,
                    1234L,
                    true,
                    createdAt
            );

            PetCreateDTO petCreationDTO = new PetCreateDTO(
                    "Buddy",
                    1L,
                    1L,
                    false,
                    "MEDIUM",
                    20.0,
                    "Brown",
                    3,
                    "Healthy and active"
            );

            PetRecordCreateDTO petRecordCreateDTO = new PetRecordCreateDTO(
                    true,
                    "Vaccinated",
                    new Date(),
                    "Observation");

            PetTypeDTO petTypeDTO = new PetTypeDTO(
                    1L,
                    "Dog",
                    1L
            );

            AdoptionRequestCreationDTO request = new AdoptionRequestCreationDTO(
                    1L,
                    1L,
                    1L
            );

            AdoptionRequestDTO adoptionRequestDTO =
                    given()
                            .contentType(ContentType.JSON)
                            .body(request)
                            .when()
                            .post("/api/v1/adoption-request")
                            .then()
                            .statusCode(201)
                            .extract().body().as(AdoptionRequestDTO.class);

            String adoptionRequestId =
                    given()
                            .contentType(ContentType.JSON)
                            .body(request)
                            .when()
                            .post("/api/v1/adoption-request")
                            .then()
                            .statusCode(201)
                            .extract().body().jsonPath().getString("id");

//            Assertions.assertEquals(adoptionRequestDTO.getId(), adoptionRequestId);

        }

    }

}