package org.mindera.fur.code.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.mindera.fur.code.dto.pet.PetRecordDTO;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.model.pet.PetBreed;
import org.mindera.fur.code.model.pet.PetType;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.pet.PetBreedRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.mindera.fur.code.repository.pet.PetTypeRepository;
import org.mindera.fur.code.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PetControllerTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    private PetService petService;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private PetTypeRepository petTypeRepository;

    @Autowired
    private PetBreedRepository petBreedRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1/pet";
        createBreedAndShelter();
    }

    @AfterEach
    void tearDown() {
        petService.deleteAllPets();
    }

    private void createBreedAndShelter() {
        if (!petBreedRepository.existsById(1L)) {
            PetBreed breed = new PetBreed();
            breed.setId(1L);
            breed.setName("Labrador");
            petBreedRepository.save(breed);
        }

        if (!shelterRepository.existsById(1L)) {
            Shelter shelter = new Shelter();
            shelter.setId(1L);
            shelter.setName("Shelter");
            shelter.setIsActive(true);
            shelter.setPhone(1234567890);
            shelter.setSize(23);
            shelter.setVat(123456789);
            shelter.setEmail("test@shelter.com");
            shelter.setAddress1("123 Main St");
            shelter.setAddress2("");
            shelter.setPostCode("12345");
            shelterRepository.save(shelter);
        }

        if (!petTypeRepository.existsById(1L)) {
            PetType petType = new PetType();
            petType.setId(1L);
            petType.setType("DOG");
            petType.setBreed(petBreedRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("Breed not found")));
            petTypeRepository.save(petType);
        }
    }

    private void createTestPets() {
        if (!petTypeRepository.existsById(2L)) {
            PetType petType = new PetType();
            petType.setId(2L);
            petType.setType("DOG");
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

    @Nested
    class CreatePetInvalidInputsTests {

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
    }

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
