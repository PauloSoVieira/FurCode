package org.mindera.fur.code;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mindera.fur.code.service.AdoptionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AdoptionRequestControllerIntegrationTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    private AdoptionRequestService adoptionRequestService;

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


        @Nested
        class validation {
            @Test
            void createAdoptionRequestWithNullAdoptionRequestId() {
                //TODO VALIDATION TESTS
            }
        }
    }
}
