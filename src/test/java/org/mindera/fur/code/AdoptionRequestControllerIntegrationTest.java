package org.mindera.fur.code;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestCreationDTO;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.pet.Pet;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.mindera.fur.code.service.AdoptionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
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
    private ShelterRepository shelterRepository;

    @Autowired
    private PetRepository petRepository;

    private Person testPerson;
    private Shelter testShelter;
    private Pet testPet;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        // Create test data
        testPerson = personRepository.save(new Person("Jojo", "jojo@example.com", 23456789L));
        testShelter = shelterRepository.save(new Shelter());
        testPet = petRepository.save(new Pet());
        testPet.setName("Fluffy");
        testShelter.setName("Mindera");
    }

    @AfterEach
    void tearDown() {
        adoptionRequestService.deleteAllAdoptionRequests();
        personRepository.deleteAll();
        shelterRepository.deleteAll();
        petRepository.deleteAll();
    }

    @Nested
    class CrudAdoptionRequest {
        @Test
        void testCreateAdoptionRequest() {
            AdoptionRequestCreationDTO creationDTO = new AdoptionRequestCreationDTO();
            creationDTO.setPetId(testPet.getId());
            creationDTO.setShelterId(testShelter.getId());
            creationDTO.setPersonId(testPerson.getId());

            given()
                    .contentType(ContentType.JSON)
                    .body(creationDTO)
                    .when()
                    .post("/api/v1/adoption-request")
                    .then()
                    .statusCode(201)
                    .body("pet.id", equalTo(testPet.getId().intValue()))
                    .body("shelter.id", equalTo(testShelter.getId().intValue()))
                    .body("person.id", equalTo(testPerson.getId().intValue()));
        }
    }
}
