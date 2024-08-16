//package org.mindera.fur.code;
//
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import org.junit.jupiter.api.*;
//import org.mindera.fur.code.dto.shelter.ShelterCreationDTO;
//import org.mindera.fur.code.dto.shelter.ShelterDTO;
//import org.mindera.fur.code.service.ShelterService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static io.restassured.RestAssured.given;
//import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
//
//@SpringBootTest(webEnvironment = RANDOM_PORT)
//public class ShelterControllerIntegrationTest {
//    @LocalServerPort
//    private Integer port;
//
//    @Autowired
//    private ShelterService shelterService;
//
//    @BeforeEach
//    void setUp() {
//        RestAssured.port = port;
//    }
//
//    @AfterEach
//    void tearDown() {
//        shelterService.deleteAllShelters();
//    }
//
//    @Nested
//    class crudShelter {
//        @Test
//        void createShelterShouldReturn201() {
//            LocalDateTime now = LocalDateTime.now();
//            ShelterCreationDTO shelter = new ShelterCreationDTO(
//                    "Shelter",
//                    123456789L,
//                    "shelter@shelter.com",
//                    "Shelter Street",
//                    "number",
//                    "4400",
//                    987654321L,
//                    1234L,
//                    true,
//                    now
//            );
//
//            String shelterId =
//                    given()
//                            .contentType(ContentType.JSON)
//                            .body(shelter)
//                            .when()
//                            .post("/api/v1/shelter")
//                            .then()
//                            .statusCode(201)
//                            .extract().body().jsonPath().getString("id");
//
//            Assertions.assertEquals(shelter.getName(), "Shelter");
//        }
//
//        @Test
//        void getShelterByIdShouldReturn200() {
//            LocalDateTime now = LocalDateTime.now();
//            ShelterCreationDTO shelterCreationDTO = new ShelterCreationDTO(
//                    "Shelter",
//                    123456789L,
//                    "shelter@shelter.com",
//                    "Shelter Street",
//                    "number",
//                    "4400",
//                    987654321L,
//                    1234L,
//                    true,
//                    now
//            );
//
//            ShelterDTO shelterDTO =
//                    given()
//                            .contentType(ContentType.JSON)
//                            .body(shelterCreationDTO)
//                            .when()
//                            .post("/api/v1/shelter")
//                            .then()
//                            .statusCode(201)
//                            .extract().body().as(ShelterDTO.class);
//
//            String shelterId =
//                    given()
//                            .contentType(ContentType.JSON)
//                            .body(shelterCreationDTO)
//                            .when()
//                            .post("/api/v1/shelter")
//                            .then()
//                            .statusCode(201).extract().body().jsonPath().getString("id");
//
//            ShelterDTO findShelterDTO =
//                    given()
//                            .when()
//                            .get("/api/v1/shelter/" + shelterId)
//                            .then()
//                            .statusCode(200).extract().body().as(ShelterDTO.class);
//        }
//
//        @Test
//        void getAllSheltersShouldReturn200() {
//            LocalDateTime now = LocalDateTime.now();
//            ShelterCreationDTO shelterCreationDTO = new ShelterCreationDTO(
//                    "Shelter",
//                    123456789L,
//                    "shelter@shelter.com",
//                    "Shelter Street",
//                    "number",
//                    "4400",
//                    987654321L,
//                    1234L,
//                    true,
//                    now
//            );
//
//            ShelterDTO shelterDTO =
//                    given()
//                            .contentType(ContentType.JSON)
//                            .body(shelterCreationDTO)
//                            .when()
//                            .post("/api/v1/shelter")
//                            .then()
//                            .statusCode(201).extract().body().as(ShelterDTO.class);
//
//            List<ShelterDTO> shelterDTOList =
//                    given()
//                            .when()
//                            .get("/api/v1/shelter/all")
//                            .then()
//                            .statusCode(200).extract().body().jsonPath().getList(".", ShelterDTO.class);
//
//            Assertions.assertEquals(1, shelterDTOList.size());
//        }
//
//        @Test
//        void updateShelterShouldReturn200() {
//            LocalDateTime now = LocalDateTime.now();
//            ShelterCreationDTO shelterCreationDTO = new ShelterCreationDTO(
//                    "Shelter",
//                    123456789L,
//                    "shelter@shelter.com",
//                    "Shelter Street",
//                    "number",
//                    "4400",
//                    987654321L,
//                    1234L,
//                    true,
//                    now
//            );
//
//            ShelterDTO shelterDTO =
//                    given()
//                            .contentType(ContentType.JSON)
//                            .body(shelterCreationDTO)
//                            .when()
//                            .post("/api/v1/shelter")
//                            .then()
//                            .statusCode(201).extract().body().as(ShelterDTO.class);
//
//            String shelterId =
//                    given()
//                            .contentType(ContentType.JSON)
//                            .body(shelterCreationDTO)
//                            .when()
//                            .post("/api/v1/shelter")
//                            .then()
//                            .statusCode(201).extract().body().jsonPath().getString("id");
//
//            shelterDTO.setEmail("shelter@shelter.com");
//            given()
//                    .contentType(ContentType.JSON)
//                    .body(shelterDTO)
//                    .patch("/api/v1/shelter/update/" + shelterId)
//                    .then()
//                    .statusCode(200).extract().body().as(ShelterDTO.class);
//
//            Assertions.assertEquals("shelter@shelter.com", shelterDTO.getEmail());
//        }
//
//        @Test
//        void deleteShelterShouldReturn204() {
//            LocalDateTime now = LocalDateTime.now();
//            ShelterCreationDTO shelterCreationDTO = new ShelterCreationDTO(
//                    "Shelter",
//                    123456789L,
//                    "shelter@shelter.com",
//                    "Shelter Street",
//                    "number",
//                    "4400",
//                    987654321L,
//                    1234L,
//                    true,
//                    now
//            );
//
//            ShelterDTO shelterDTO =
//                    given()
//                            .contentType(ContentType.JSON)
//                            .body(shelterCreationDTO)
//                            .when()
//                            .post("/api/v1/shelter")
//                            .then()
//                            .statusCode(201).extract().body().as(ShelterDTO.class);
//
//            String shelterId =
//                    given()
//                            .contentType(ContentType.JSON)
//                            .body(shelterCreationDTO)
//                            .when()
//                            .post("/api/v1/shelter")
//                            .then()
//                            .statusCode(201).extract().body().jsonPath().getString("id");
//
//            given()
//                    .when()
//                    .delete("/api/v1/shelter/delete/" + shelterId)
//                    .then()
//                    .statusCode(204);
//        }
//    }
//
//    @Nested
//    class validation {
//        @Test
//        void createShelterWithNullName() {
//            //TODO VALIDATION TESTS
//        }
//    }
//}
